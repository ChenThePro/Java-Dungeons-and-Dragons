# 🐉 Dungeons and Dragons CLI Game

## 📖 Introduction

This is a single-player, multi-level Dungeons and Dragons-themed CLI game developed in Java. You control a heroic character navigating dungeons, defeating enemies, and progressing through increasingly difficult levels. The project follows object-oriented design principles and implements patterns such as Visitor and Observer to ensure clean architecture and maintainability.

---

## 📚 Table of Contents

- [Features](#-features)
- [Installation](#-installation)
- [Usage](#-usage)
- [Game Mechanics](#-game-mechanics)
  - [Game Flow](#game-flow)
  - [Tiles](#tiles)
  - [Units](#units)
  - [Combat System](#combat-system)
  - [CLI Controls](#cli-controls)
- [Bonus Features](#-bonus-features)
- [Dependencies](#-dependencies)
- [Testing](#-testing)
- [Contributors](#-contributors)
- [License](#-license)

---

## ✨ Features

- Fully interactive command-line gameplay
- Multiple player classes: Warrior, Mage, Rogue, **Hunter (bonus)**
- Multiple enemy types: Monsters, Traps, **Bosses (bonus)**
- Object-oriented implementation using inheritance and interfaces
- Implements Visitor and Observer design patterns
- Turn-based combat system
- Level progression and character leveling
- Visual 2D dungeon map with ASCII graphics
- Expandable architecture for additional classes and enemies

---

## 🛠️ Installation

### Requirements

- Java 8 or above
- Command-line environment

### Steps

1. Clone the repository or unzip your `id1_id2.zip` submission.
2. Compile or use the provided `hw3.jar` file.
3. Run the game using:

```bash
java -jar hw3.jar <Path to level files>
```

Example:

```bash
java -jar hw3.jar ./levels/
```

---

## ▶️ Usage

Upon launching the game:

1. Select your character from a list of predefined heroes.
2. Use keyboard commands to move, fight enemies, and cast abilities.
3. Clear all enemies on the level to proceed.
4. Defeat all levels to win the game. Death ends the game.

---

## 🎮 Game Mechanics

### Game Flow

- Each level is a map file (e.g., `level1.txt`) loaded sequentially.
- Player and enemies take turns each "tick."
- The game ends when the player clears all levels or dies.

### Tiles

- `.` — Walkable tile  
- `#` — Wall (blocked)  
- `@` — Player  
- `X` — Dead player  
- Other characters — Enemy units  

### Units

#### Player Types

- **Warrior**: Strong melee fighter with healing and cooldown-based ability.
- **Mage**: Area caster with mana-based blizzard attacks.
- **Rogue**: Agile fighter with energy-based AoE strikes.
- **Hunter** (Bonus): Ranged attacker using arrows.

#### Enemies

- **Monsters**: Chase and attack player.
- **Traps**: Invisible/visible, stationary attackers.
- **Bosses** (Bonus): Enemy units with special abilities, implementing `HeroicUnit`.

### Combat System

1. Attacker rolls (0 to attack power)
2. Defender rolls (0 to defense)
3. Damage = attacker - defender (if > 0)
4. Health reduced accordingly
5. Death triggers experience gain or game over

### CLI Controls

| Key | Action            |
|-----|-------------------|
| `w` | Move Up           |
| `s` | Move Down         |
| `a` | Move Left         |
| `d` | Move Right        |
| `e` | Cast Special      |
| `q` | Do Nothing        |

---

## 🧩 Bonus Features

### ➕ New Player Class: Hunter

- **Name**: Ygritte
- **Attack**: Ranged attack using arrows
- **Ability**: Shoot the nearest enemy within range
- **Resource**: Arrows (regenerated over time)

### ➕ New Enemy Class: Boss

- Implements `HeroicUnit` with a unique attack (`Shoebodybop`)
- Existing bosses updated:
  - **The Mountain**
  - **Queen Cersei**
  - **Night’s King**

---

## 📦 Dependencies

- Java Standard Library
- No external dependencies required

---

## 🧪 Testing

- Includes unit tests for:
  - Combat interactions
  - Leveling and experience gain
  - Ability usage
  - Range and movement logic
- Edge case handling for:
  - Resource depletion
  - Simultaneous deaths
  - Ability cooldowns and visibility mechanics

---

## 👤 Contributors

- **Author**: [Your Name Here]  
  _Completed core implementation and bonus features._

---

## 📄 License

This project is licensed for academic use only. Contact the author for other licensing inquiries.
