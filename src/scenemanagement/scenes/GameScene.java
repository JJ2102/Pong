package scenemanagement.scenes;

import enums.Difficulty;
import enums.OverlayType;
import hitboxes.BoxHitbox;
import math.Vector2;
import math.Vector3;
import objects.*;
import rendering.Camera;
import rendering.Renderer;
import scenemanagement.GameWindow;
import utility.Cooldown;
import utility.Countdown;
import utility.Globals;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

// Die eigentliche Spielszene, in der das 3D-Pong gespielt wird
public class GameScene extends Scene {
    private static final int WINNING_SCORE = 4; // Punkte, die zum Sieg nötig sind
    // halbe Tiefe des Spielfelds (von der Mitte bis zu einer Wand)
    private static final double BOX_DEPTH = 1.5;
    // Z-Ebene, auf der sich das Spieler-Paddle bewegt (knapp vor der Wand)
    private static final double PLAYER_POSITION_Z = -BOX_DEPTH + 0.2;

    // Renderer
    private final transient Renderer renderer; // rechnet die 3D-Objekte auf die 2D-Zeichenfläche um
    private final transient Camera camera; // Blickpunkt, aus dem die Szene gezeichnet wird

    // Objekte
    private final transient Player player; // Paddle des Spielers (folgt der Maus)
    private final transient Enemy aiPlayer; // Paddle der KI
    private final transient Box box; // Der Spielraum bzw. die Wände drumherum
    private final transient Ball ball;

    // KoordinatenAchsen
    private final transient Vector xAxis;
    private final transient Vector yAxis;
    private final transient Vector zAxis;

    // Punkteanzeige, die als 3D-Objekt an der Seitenwand hängt
    private final transient SevenSegmentDisplay scoreDisplay;

    // Hitboxes
    private final transient BoxHitbox goalPlayerHitbox; // Torfläche vor dem Spieler, Treffer = Punkt für die KI
    private final transient BoxHitbox goalAiHitbox; // Torfläche hinter der KI, Treffer = Punkt für den Spieler

    // Hit Cooldown
    private final transient Cooldown hitCooldown; // Sperrzeit nach einem Paddle-Treffer

    // Scoring
    private final transient Countdown countdown; // Countdown vor jeder Runde

    // Difficulty
    private Difficulty gameDifficulty = Difficulty.MEDIUM; // legt fest, wie schnell die KI dem Ball folgen darf

    // FPS
    private long lastFrameTime = System.currentTimeMillis(); // Zeitstempel des letzten Frames
    private int fps = 0; // zuletzt berechnete Bilder pro Sekunde, wird nur im Debug-Modus angezeigt

    // Score
    private int playerScore = 0;
    private int aiScore = 0;

    private GameState gameState = GameState.COUNTING_DOWN; // Das Spiel beginnt immer mit einem Countdown

    // Positionen
    private transient Vector3 mousePosition = new Vector3(0, 0, PLAYER_POSITION_Z); // Aktuelle Mausposition im 3D-Raum

    // Initialisiert Renderer, Kamera, Box, Ball, Spieler und Gegner sowie die Tore
    public GameScene(GameWindow window) {
        super(window);
        setCursor(Globals.getInvisibleCursor()); // Mauszeiger im Spiel unsichtbar machen

        // Kamera und Renderer initialisieren
        camera = new Camera();
        double cameraPositionZ = -BOX_DEPTH - 1;
        // Kamera leicht hinter der vorderen Box-Wand platzieren
        camera.setPosition(new Vector3(0, 0, cameraPositionZ));
        // Der Renderer zeichnet ab jetzt immer aus Sicht dieser Kamera
        renderer = new Renderer(getWidth(), getHeight(), camera);

        // Koordinatensystem
        xAxis = new Vector(new Vector3(0, 0, 0), new Vector3(1, 0, 0), 1, Color.RED);
        yAxis = new Vector(new Vector3(0, 0, 0), new Vector3(0, 1, 0), 1, Color.GREEN);
        zAxis = new Vector(new Vector3(0, 0, 0), new Vector3(0, 0, 1), 1, Color.BLUE);

        // Spielobjekte initialisieren
        box = new Box(BOX_DEPTH);
        ball = new Ball(box.getSize());

        // Score Display (7-Segment) konfigurieren und in der Welt platzieren
        scoreDisplay = new SevenSegmentDisplay();
        scoreDisplay.getTransform().setScale(new Vector3(0.5, 0.5, 0.5));
        scoreDisplay.getTransform().setPosition(new Vector3(box.getSize().getX() - 0.1, 0, -0.5));
        scoreDisplay.getTransform().setRotation(new Vector3(0, Math.toRadians(90), 0));

        // 3 Sekunden Countdown (für den Start jeder Runde) erstellen
        countdown = new Countdown(3000);
        countdown.onTick(_ -> repaint()); // Bei jedem Tick neu zeichnen, damit die Zahl sichtbar runterzählt
        countdown.onFinish(() -> {
            gameState = GameState.PLAYING;
            window.showOverlay(OverlayType.SHATTERED_GLASS, false); // Ggf. zerbrochenes Glas ausblenden
        });

        // Cooldown für Paddle-Treffer (verhindert mehrfache Kollisionserkennung im selben Frame)
        hitCooldown = new Cooldown(120);

        // Hitboxes für Tore generieren (an der Vorder- und Rückwand der Box)
        Vector3 boxSize = box.getSize();
        Vector3 hitboxSize = new Vector3(boxSize.getX() * 2, boxSize.getY() * 2, 0);
        goalPlayerHitbox = new BoxHitbox(new Vector3(0, 0, -BOX_DEPTH), hitboxSize);
        goalAiHitbox = new BoxHitbox(new Vector3(0, 0, BOX_DEPTH), hitboxSize);

        // Spieler-Paddle vorne platzieren
        player = new Player(new Vector3(0, 0, PLAYER_POSITION_Z));

        // KI-Paddle spiegelverkehrt hinten platzieren
        Vector3 playerPosition = player.getTransform().getPosition();
        Vector3 aiPosition = new Vector3(playerPosition.getX(), playerPosition.getY(), -PLAYER_POSITION_Z);
        aiPlayer = new Enemy(aiPosition, box.getSize());
    }

    // ===== Spiel-Logik =====

    // Wird in jedem Frame aufgerufen: Aktualisiert Positionen, prüft Kollisionen und Tore
    @Override
    protected void update() {
        if (window.isDebug()) {
            calculateFps(); // FPS nur mitzählen, wenn sie auch angezeigt werden
        }

        // Spieler folgt der Maus
        player.moveTo(mousePosition);

        // Leichte Kamera-Bewegung basierend auf der Mausposition für Parallaxen-Effekt
        Vector3 targetCameraPosition = mousePosition.divide(15);
        Vector3 cameraPosition = camera.getPosition().leap(targetCameraPosition, 0.5);
        camera.setPosition(new Vector3(cameraPosition.getX(), cameraPosition.getY(), camera.getPosition().getZ()));

        // Während des Countdowns bewegen sich nur Spieler und Kamera, der Rest bleibt stehen
        if (gameState == GameState.COUNTING_DOWN) {
            return;
        }

        // Kollision des Balls mit den beiden Paddles prüfen
        BoxHitbox[] paddleHitboxes = new BoxHitbox[]{player.getHitbox(), aiPlayer.getHitbox()};
        if (hitCooldown.isReady() && ball.paddleHit(paddleHitboxes)) {
            window.getSoundManager().playSoundEffect("pong");
            hitCooldown.trigger(); // Verhindert, dass der Treffer sofort mehrfach ausgelöst wird
        }

        // Ball entsprechend seiner Geschwindigkeit bewegen
        ball.move();

        // Tore prüfen (hat der Ball die vordere oder hintere Wand erreicht?)
        if (goalPlayerHitbox.intersects(ball.getHitbox())) { // Ball trifft vorne → Punkt für KI
            addPoint(PlayerType.AI);
        }
        if (goalAiHitbox.intersects(ball.getHitbox())) { // Ball trifft hinten → Punkt für Spieler
            addPoint(PlayerType.PLAYER);
        }

        // KI bewegt sich in Richtung des Balls (Geschwindigkeit ist durch Difficulty begrenzt)
        aiPlayer.move(ball.getTransform().getPosition(), gameDifficulty.getValue());
    }

    // Zählt einen Punkt für den Torschützen, aktualisiert das UI und prüft auf Sieg
    private void addPoint(PlayerType scorer) {
        if (scorer == PlayerType.AI) {
            aiScore++;
            // Bildschirm brechen-Effekt am aktuellen Ort des Balls erzeugen
            Vector2 ballScreenPosition = renderer.worldToScreen(ball.getTransform().getPosition());
            window.getShatteredGlassOverlay().generateShatter(
                    (int) ballScreenPosition.getX(), (int) ballScreenPosition.getY(),
                    window.getWidth(), window.getHeight());
            window.showOverlay(OverlayType.SHATTERED_GLASS, true);
        } else {
            playerScore++;
        }

        window.getSoundManager().playSoundEffect("score");
        scoreDisplay.setScore(aiScore, playerScore); // Punktetafel aktualisieren
        ball.reset(); // Ball zurück in die Mitte

        // Überprüfen, ob jemand die nötigen Punkte zum Sieg erreicht hat
        if (aiScore == WINNING_SCORE || playerScore == WINNING_SCORE) {
            if (aiScore == WINNING_SCORE) {
                window.toggleOverlay(OverlayType.LOSE);
                window.getSoundManager().playSoundEffect("lose");
            } else {
                window.toggleOverlay(OverlayType.WIN);
                window.getSoundManager().playSoundEffect("win");
            }
            return;
        }
        aiPlayer.reset();

        // Wenn noch niemand gewonnen hat, startet der Countdown für die nächste Runde
        gameState = GameState.COUNTING_DOWN;
        countdown.restart();
    }

    // Setzt den Spielstand, Ball und Gegner für ein komplett neues Spiel zurück
    public void reset() {
        playerScore = 0;
        aiScore = 0;
        scoreDisplay.setScore(aiScore, playerScore);
        ball.reset();
        aiPlayer.reset();
    }

    // ===== Laufzeit (Lifecycle) =====

    // Friert das Spiel ein (z.B. beim Öffnen des Pausenmenüs): Game-Loop und Countdown anhalten
    @Override
    public void onPause() {
        if (getTimer().isRunning()) {
            getTimer().stop();
        }
        if (countdown.isRunning()) {
            countdown.stop();
        }
    }

    // Lässt das Spiel weiterlaufen, ein unterbrochener Countdown wird dabei fortgesetzt
    @Override
    public void onResume() {
        if (!getTimer().isRunning()) {
            getTimer().start();
        }
        if (gameState == GameState.COUNTING_DOWN && !countdown.isRunning()) {
            countdown.start();
        }
    }

    // Startet eine komplett neue Partie: Punkte zurücksetzen und mit dem Countdown beginnen
    public void restart() {
        reset();
        gameState = GameState.COUNTING_DOWN;
        countdown.restart();
        onResume();
    }

    // ===== Rendering =====

    // Zeichnet die gesamte Szene: 3D-Objekte, Punktestand, Countdown und im Debug-Modus die Hitboxes
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        renderer.updateSize(getWidth(), getHeight()); // Zeichenfläche kann beim ersten Frame noch 0 gewesen sein

        // Reihenfolge beachten: Von hinten nach vorne rendern
        renderer.renderEntity(g2d, box);
        renderer.renderEntity(g2d, aiPlayer);
        renderer.renderEntity(g2d, scoreDisplay);
        renderer.renderEntity(g2d, ball);
        renderer.renderEntity(g2d, player);

        if (window.isDebug()) {
            // Koordinatensystem rendern (X=Rot, Y=Grün, Z=Blau)
            renderer.renderEntity(g2d, xAxis);
            renderer.renderEntity(g2d, yAxis);
            renderer.renderEntity(g2d, zAxis);

            // Debug-Modus: Alle Hitboxes (Paddles, Tore, Ball) anzeigen
            renderer.renderBoxHitbox(g2d, ball.getHitbox(), Color.BLUE);
            renderer.renderBoxHitbox(g2d, aiPlayer.getHitbox(), Color.YELLOW);
            renderer.renderBoxHitbox(g2d, goalAiHitbox, Color.RED);
            renderer.renderBoxHitbox(g2d, player.getHitbox(), Color.YELLOW);
            renderer.renderBoxHitbox(g2d, goalPlayerHitbox, Color.RED);

            // FPS oben links anzeigen
            g2d.setFont(new Font("Monospace", Font.BOLD, 20));
            g2d.setColor(Color.GREEN);
            g2d.drawString("FPS: " + fps, 10, 20);
        }

        // Punktestand zentriert oben einblenden
        g2d.setFont(Globals.getMainFont(36));
        g2d.setColor(Color.GREEN);
        String scoreText = playerScore + " : " + aiScore;
        FontMetrics fm = g2d.getFontMetrics(); // Liefert die Maße des aktuellen Fonts
        int textWidth = fm.stringWidth(scoreText); // Textbreite wird zum Zentrieren gebraucht
        g2d.drawString(scoreText, (getWidth() - textWidth) / 2, 50);

        // Countdown groß in der Mitte einblenden
        if (gameState == GameState.COUNTING_DOWN && countdown.isRunning()) {
            g2d.setFont(Globals.getMainFont(120));
            g2d.setColor(Globals.getFontColor(200));
            String text = String.valueOf(countdown.getRemainingSeconds());

            // Auch hier wird der Text über seine Breite horizontal zentriert
            fm = g2d.getFontMetrics();
            textWidth = fm.stringWidth(text);
            g2d.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2);
        }
    }

    // ===== Key/MouseListener Methoden =====

    // Merkt sich bei jeder Mausbewegung die Zielposition für das Spieler-Paddle
    @Override
    public void mouseMoved(MouseEvent e) {
        // Wandelt 2D-Maus-Pixel-Koordinaten in 3D-Weltkoordinaten für das Paddle um
        Vector2 mouseScreenPosition = new Vector2(e.getX(), e.getY());
        mousePosition = renderer.screenToWorld(mouseScreenPosition, PLAYER_POSITION_Z);
    }

    // Tastenkürzel im Spiel: Escape öffnet/schließt die Pause, F3 schaltet die Debug-Ansicht um
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            window.toggleOverlay(OverlayType.PAUSE); // Pausenmenü
        }
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            window.toggleDebug(); // Debug-Ansicht
        }
    }

    // ===== Getter und Setter =====
    public void setDifficulty(Difficulty difficulty) {
        this.gameDifficulty = difficulty;
    }

    // ===== Debug Methoden =====

    // Misst die Zeit seit dem letzten Frame und rechnet sie in Bilder pro Sekunde um
    private void calculateFps() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastFrameTime; // Vergangene Zeit seit dem letzten Aufruf in ms
        lastFrameTime = currentTime;

        if (deltaTime > 0) { // Division durch 0 abfangen, falls zwei Frames in dieselbe Millisekunde fallen
            fps = (int) (1000 / deltaTime);
        }
    }

    // ===== Verschachtelte Typen =====

    // unterscheidet, für wen ein Punkt gezählt wird
    private enum PlayerType { PLAYER, AI }

    // läuft das Spiel gerade, oder wartet es auf den Rundenstart?
    private enum GameState { PLAYING, COUNTING_DOWN }
}
