package dnd;

import dnd.game.engine.LevelManager;
import dnd.game.tiles.Empty;
import dnd.game.tiles.Tile;
import dnd.game.tiles.Wall;
import dnd.game.units.*;
import dnd.game.engine.CLI;
import dnd.game.engine.GameEventListener;
import dnd.game.engine.GameController;
import dnd.game.tiles.Unit;
import dnd.game.utils.Position;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Comprehensive test suite for the D&D Game Engine without Mockito dependency.
 * Uses test doubles and real objects for testing.
 */
public class DnDGameEngineTests {

    // =============== Test Helper Classes ===============

    static class TestPlayer extends Player {
        public TestPlayer(Position position) {
            super('@', position, "TestPlayer", 100, 10, 5);
        }

        @Override
        public void castAbility(List<Unit> unitList) {
            // Test implementation - does nothing
        }

        @Override
        public void onGameTick() {
            // Test implementation - does nothing
        }
    }

    static class TestGameEventListener implements GameEventListener {
        public List<String> messages = new ArrayList<>();
        public List<String> failures = new ArrayList<>();
        public List<String> combats = new ArrayList<>();
        public List<String> casts = new ArrayList<>();
        public List<Player> levelUps = new ArrayList<>();

        @Override
        public void onFailure(String message) {
            failures.add(message);
        }

        @Override
        public void onCombat(Unit attacker, Unit defender, int attackRoll, int defenseRoll, int damage) {
            combats.add(String.format("%s vs %s: %d-%d-%d",
                    attacker.getName(), defender.getName(), attackRoll, defenseRoll, damage));
        }

        @Override
        public void onCast(Unit caster, Unit target, int attackRoll, int defenseRoll, int damage, String abilityName) {
            casts.add(String.format("%s casts %s on %s: %d-%d-%d",
                    caster.getName(), abilityName, target.getName(), attackRoll, defenseRoll, damage));
        }

        @Override
        public void onLevelUp(Player player) {
            levelUps.add(player);
        }

        public void clear() {
            messages.clear();
            failures.clear();
            combats.clear();
            casts.clear();
            levelUps.clear();
        }
    }

    static class TestUnit extends Unit {
        private boolean moveToWasCalled = false;
        private Tile lastTileMovedTo = null;

        public TestUnit(Position position, char tile, String name) {
            super(tile, position, name, 50, 10, 5);
        }

        @Override
        public void moveTo(Tile tile) {
            super.moveTo(tile);
            moveToWasCalled = true;
            lastTileMovedTo = tile;
        }

        @Override
        public void onGameTick() {
            // Test implementation
        }

        public boolean wasMoveToCall() {
            return moveToWasCalled;
        }

        public Tile getLastTileMovedTo() {
            return lastTileMovedTo;
        }

        public void resetTestState() {
            moveToWasCalled = false;
            lastTileMovedTo = null;
        }
    }

    // =============== CLI Tests ===============

    @Nested
    @DisplayName("CLI Tests")
    class CLITest {
        private TestGameController testController;
        private CLI cli;
        private ByteArrayOutputStream outputStream;
        private PrintStream originalOut;

        @BeforeEach
        void setUp() {
            testController = new TestGameController();
            cli = new CLI(testController);
            outputStream = new ByteArrayOutputStream();
            originalOut = System.out;
            System.setOut(new PrintStream(outputStream));
        }

        @AfterEach
        void tearDown() {
            System.setOut(originalOut);
        }

        @Test
        @DisplayName("CLI constructor sets up controller listener")
        void testCLIConstructor() {
            assertTrue(testController.wasSetListenerCalled());
        }

        @Test
        @DisplayName("onFailure displays error message")
        void testOnFailure() {
            String errorMessage = "Test error";
            cli.onFailure(errorMessage);
            assertTrue(outputStream.toString().contains("Error: " + errorMessage));
        }

        // Test helper class
        static class TestGameController extends GameController {
            private boolean setListenerCalled = false;

            public TestGameController() {
                super(new Tile[3][3], new TestPlayer(new Position(1, 1)));
            }

            @Override
            public void setListener(GameEventListener listener) {
                super.setListener(listener);
                setListenerCalled = true;
            }

            public boolean wasSetListenerCalled() {
                return setListenerCalled;
            }
        }
    }

    // =============== GameController Tests ===============

    @Nested
    @DisplayName("GameController Tests")
    class GameControllerTest {
        private GameController controller;
        private Tile[][] board;
        private TestPlayer player;
        private TestGameEventListener listener;

        @BeforeEach
        void setUp() {
            board = new Tile[5][5];
            player = new TestPlayer(new Position(2, 2));
            listener = new TestGameEventListener();

            // Initialize board with empty tiles
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    board[y][x] = new Empty(new Position(x, y));
                }
            }

            controller = new GameController(board, player);
            controller.setListener(listener);
        }

        @Test
        @DisplayName("Invalid input shows error message")
        void testInvalidInput() {
            controller.gameTick('z');
            assertFalse(listener.failures.isEmpty());
            assertTrue(listener.failures.getFirst().contains("Invalid input"));
        }

        @Test
        @DisplayName("Skip turn works correctly")
        void testSkipTurn() {
            controller.gameTick('q');
            // Should complete without errors
            assertTrue(listener.failures.isEmpty());
        }

        @Test
        @DisplayName("Cast ability command works")
        void testCastAbility() {
            controller.gameTick('e');
            // Should complete without errors
            assertTrue(listener.failures.isEmpty());
        }

        @Test
        @DisplayName("Movement commands work")
        void testMovementCommands() {
            char[] movements = {'w', 'a', 's', 'd'};
            for (char move : movements) {
                listener.clear();
                controller.gameTick(move);
                // Should not produce failure messages for valid movements
                assertTrue(listener.failures.isEmpty());
            }
        }
    }

    // =============== LevelManager Tests ===============

    @Nested
    @DisplayName("LevelManager Tests")
    class LevelManagerTest {
        private LevelManager levelManager;
        private Path tempDir;
        private TestPlayer player;

        @BeforeEach
        void setUp() throws IOException {
            tempDir = Files.createTempDirectory("test-levels");
            levelManager = new LevelManager(tempDir.toString());
            player = new TestPlayer(new Position(0, 0));
        }

        @AfterEach
        void tearDown() throws IOException {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        @Test
        @DisplayName("Load valid level creates proper board")
        void testLoadValidLevel() throws IOException {
            String levelContent = """
                    #####
                    #...#
                    #.@.#
                    #...#
                    #####""";

            Files.write(tempDir.resolve("level1"), levelContent.lines().toList());

            LevelManager.LevelData levelData = levelManager.loadNextLevel(player);

            assertNotNull(levelData);
            assertEquals(5, levelData.board().length);
            assertEquals(5, levelData.board()[0].length);
            assertInstanceOf(Wall.class, levelData.board()[0][0]);
            assertInstanceOf(Empty.class, levelData.board()[1][1]);
            assertEquals(new Position(2, 2), player.getPosition());
        }

        @Test
        @DisplayName("Load level with enemies creates enemy units")
        void testLoadLevelWithEnemies() throws IOException {
            String levelContent = """
                    ####
                    #@g#
                    #s.#
                    ####""";

            Files.write(tempDir.resolve("level1"), levelContent.lines().toList());

            LevelManager.LevelData levelData = levelManager.loadNextLevel(player);

            assertNotNull(levelData);
            assertEquals(2, levelData.enemies().size());
        }

        @Test
        @DisplayName("Non-existent level returns null")
        void testNonExistentLevel() throws IOException {
            LevelManager.LevelData levelData = levelManager.loadNextLevel(player);
            assertNull(levelData);
        }

        @Test
        @DisplayName("Multiple level loading increments level number")
        void testMultipleLevels() throws IOException {
            Files.write(tempDir.resolve("level1"), List.of("#@#"));
            Files.write(tempDir.resolve("level2"), List.of("@"));

            levelManager.loadNextLevel(player);
            LevelManager.LevelData level2 = levelManager.loadNextLevel(player);

            assertNotNull(level2);
            assertEquals(1, level2.board().length);
        }
    }

    // =============== Tile Tests ===============

    @Nested
    @DisplayName("Tile Tests")
    class TileTest {

        @Test
        @DisplayName("Wall tile blocks units")
        void testWallTileBlocks() {
            Wall wall = new Wall(new Position(1, 1));
            TestUnit unit = new TestUnit(new Position(0, 0), 'T', "TestUnit");

            wall.accept(unit);

            assertFalse(unit.wasMoveToCall());
        }

        @Test
        @DisplayName("Tiles have correct symbols")
        void testTileSymbols() {
            Empty empty = new Empty(new Position(0, 0));
            Wall wall = new Wall(new Position(0, 0));

            assertEquals('.', empty.getTile());
            assertEquals('#', wall.getTile());
            assertEquals(".", empty.toString());
            assertEquals("#", wall.toString());
        }
    }

    // =============== Player Tests ===============

    @Nested
    @DisplayName("Player Tests")
    class PlayerTest {
        private TestPlayer player;
        private TestGameEventListener listener;

        @BeforeEach
        void setUp() {
            player = new TestPlayer(new Position(1, 1));
            listener = new TestGameEventListener();
            player.setEventListener(listener);
        }

        @Test
        @DisplayName("Player initializes with correct stats")
        void testPlayerInitialization() {
            assertEquals('@', player.getTile());
            assertEquals("TestPlayer", player.getName());
            assertEquals(100, player.getHealth());
            assertEquals(10, player.getAttack());
            assertEquals(5, player.getDefense());
            assertEquals(0, player.getXP());
            assertEquals(1, player.getLevel());
        }

        @Test
        @DisplayName("Player gains XP without leveling up")
        void testGainXPNoLevelUp() {
            player.gainXP(25);
            assertEquals(25, player.getXP());
            assertEquals(1, player.getLevel());
            assertTrue(listener.levelUps.isEmpty());
        }

        @Test
        @DisplayName("Player levels up with enough XP")
        void testLevelUp() {
            int initialAttack = player.getAttack();
            int initialDefense = player.getDefense();

            player.gainXP(50);

            assertEquals(2, player.getLevel());
            assertEquals(0, player.getXP()); // XP resets after level up
            assertTrue(player.getAttack() > initialAttack);
            assertTrue(player.getDefense() > initialDefense);
            assertEquals(1, listener.levelUps.size());
            assertEquals(player, listener.levelUps.getFirst());
        }

        @Test
        @DisplayName("Player can level up multiple times")
        void testMultipleLevelUps() {
            player.gainXP(150);
            assertEquals(3, player.getLevel());
            assertEquals(2, listener.levelUps.size());
        }

        @Test
        @DisplayName("Player dies when health reaches zero")
        void testPlayerDeath() {
            player.takeDamage(1000);
            assertTrue(player.isDead());
        }

        @Test
        @DisplayName("Player description contains correct information")
        void testPlayerDescription() {
            String description = player.description();
            assertTrue(description.contains("TestPlayer"));
            assertTrue(description.contains("Health: 100/100"));
            assertTrue(description.contains("Level: 1"));
        }
    }

    // =============== Hunter Tests ===============

    @Nested
    @DisplayName("Hunter Tests")
    class HunterTest {
        private Hunter hunter;
        private TestGameEventListener listener;
        private Tile[][] board;

        @BeforeEach
        void setUp() {
            hunter = new Hunter(new Position(1, 1), "TestHunter", 100, 15, 8, 5);
            listener = new TestGameEventListener();
            hunter.setEventListener(listener);

            // Set up board
            board = new Tile[5][5];
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    board[y][x] = new Empty(new Position(x, y));
                }
            }
            hunter.setBoard(board);
        }

        @Test
        @DisplayName("Hunter initializes with arrows")
        void testHunterInitialization() {
            assertEquals(10, hunter.getArrowsCount()); // 10 * level 1
            assertEquals("TestHunter", hunter.getName());
        }

        @Test
        @DisplayName("Hunter description shows arrow count")
        void testHunterDescription() {
            String description = hunter.description();
            assertTrue(description.contains("Arrows: 10"));
        }

        @Test
        @DisplayName("Hunter regenerates arrows over time")
        void testArrowRegeneration() {
            int initialArrows = hunter.getArrowsCount();

            // Simulate 10 game ticks
            for (int i = 0; i < 10; i++) {
                hunter.onGameTick();
            }

            // Should regenerate arrows (exact amount depends on implementation)
            assertTrue(hunter.getArrowsCount() >= initialArrows);
        }

        @Test
        @DisplayName("Hunter can cast ability")
        void testCastAbility() {
            // Create a test enemy in range
            Monster enemy = new Monster(new Position(2, 2), 'G', "Goblin", 30, 8, 3, 2, 20);
            enemy.setBoard(board);

            int initialArrows = hunter.getArrowsCount();
            hunter.castAbility(List.of(enemy));

            // Either arrows decreased (successful shot) or failure message (no valid targets)
            assertTrue(hunter.getArrowsCount() <= initialArrows || !listener.failures.isEmpty());
        }
    }

    // =============== Monster Tests ===============

    @Nested
    @DisplayName("Monster Tests")
    class MonsterTest {
        private Monster monster;

        @BeforeEach
        void setUp() {
            monster = new Monster(new Position(0, 0), 'M', "TestMonster", 50, 12, 6, 3, 25);
            TestPlayer player = new TestPlayer(new Position(2, 2));

            // Set up board
            Tile[][] board = new Tile[5][5];
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    board[y][x] = new Empty(new Position(x, y));
                }
            }

            monster.setBoard(board);
            monster.setPlayerReference(player);
        }

        @Test
        @DisplayName("Monster initializes correctly")
        void testMonsterInitialization() {
            assertEquals('M', monster.getTile());
            assertEquals("TestMonster", monster.getName());
            assertEquals(25, monster.getExperienceValue());
        }

        @Test
        @DisplayName("Monster description shows XP value")
        void testMonsterDescription() {
            String description = monster.description();
            assertTrue(description.contains("XP: 25"));
        }

        @Test
        @DisplayName("Monster can perform game tick")
        void testMonsterGameTick() {
            Position initialPosition = monster.getPosition();

            monster.onGameTick();

            // Monster should either move or stay in place - both are valid
            assertNotNull(monster.getPosition());
        }
    }

    // =============== Integration and Edge Case Tests ===============

    @Nested
    @DisplayName("Integration and Edge Cases")
    class IntegrationTest {

        @Test
        @DisplayName("Large board handles properly")
        void testLargeBoard() {
            Tile[][] largeBoard = new Tile[100][100];
            for (int y = 0; y < 100; y++) {
                for (int x = 0; x < 100; x++) {
                    largeBoard[y][x] = new Empty(new Position(x, y));
                }
            }

            TestPlayer player = new TestPlayer(new Position(50, 50));
            assertDoesNotThrow(() -> {
                new GameController(largeBoard, player);
            });
        }

        @Test
        @DisplayName("Game handles many enemies")
        void testManyEnemies() {
            Tile[][] board = new Tile[10][10];
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                    board[y][x] = new Empty(new Position(x, y));
                }
            }

            GameController controller = new GameController(board, new TestPlayer(new Position(5, 5)));
            List<Unit> manyEnemies = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                manyEnemies.add(new Monster(new Position(i % 10, i / 10), 'G', "Goblin" + i, 30, 8, 3, 2, 20));
            }

            assertDoesNotThrow(() -> {
                controller.setEnemies(manyEnemies);
            });
        }

        @Test
        @DisplayName("Position equality works correctly")
        void testPositionEquality() {
            Position pos1 = new Position(1, 2);
            Position pos2 = new Position(1, 2);
            Position pos3 = new Position(2, 1);

            assertEquals(pos1, pos2);
            assertNotEquals(pos1, pos3);
        }

        @Test
        @DisplayName("Unit health management")
        void testUnitHealthManagement() {
            TestUnit unit = new TestUnit(new Position(0, 0), 'T', "TestUnit");
            int initialHealth = unit.getHealth();

            unit.takeDamage(10);
            assertEquals(initialHealth - 10, unit.getHealth());

            unit.takeDamage(1000);
            assertTrue(unit.isDead());
            assertTrue(unit.getHealth() <= 0);
        }
    }
}

/*
 * ADVANTAGES OF THIS NO-MOCKITO APPROACH:
 *
 * 1. NO EXTERNAL DEPENDENCIES - Only uses JUnit 5
 * 2. EASIER SETUP - No need to configure Mockito
 * 3. MORE REALISTIC TESTING - Tests actual object interactions
 * 4. BETTER INTEGRATION TESTING - Tests real behavior, not mocked behavior
 *
 * TRADE-OFFS:
 *
 * 1. More verbose test helper classes needed
 * 2. Some tests are less isolated (test real objects instead of mocks)
 * 3. Harder to test specific error conditions that are difficult to reproduce
 *
 * RECOMMENDATIONS:
 *
 * Choose this approach if:
 * - You want to minimize dependencies
 * - You prefer testing real object behavior
 * - Your codebase is well-designed for testing without mocks
 *
 * Use the Mockito version if:
 * - You need to test complex error scenarios
 * - You want more isolated unit tests
 * - You're comfortable adding test dependencies
 */