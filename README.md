# Knight Runner

A 2D endless runner built in Java with Swing. Guide a knight across an endless
landscape, jumping over cacti, stones, and barrels as the game gets faster.
Every 15 points a magic portal appears. Run through it to spin the ability wheel
and win a temporary power-up.

## Features
- **Endless runner gameplay** with a 60 FPS game loop and gravity-based jumping
- **Animated knight** with separate run, jump, and death animations
- **Randomized obstacles** (cacti, stones, barrels) with spacing checks so they
  never spawn unfairly close together
- **Rising difficulty**: obstacle speed increases as your score climbs
- **Portal and ability wheel**: every 15 points a portal spawns. Entering it
  clears the screen and spins a wheel that slows to a stop on a random reward:
  - **Invincibility**: pass through obstacles for 5 seconds
  - **Jump Boost**: 20% higher jumps for 5 seconds
- **Score and high score** tracking, with a game-over screen and instant restart
- **Sound design**: background music plus jump, power-up, and death effects
- **Scales to any screen size**: sprites, fonts, and speeds adjust to the window
- **Custom pixel font** and a start screen

## Controls
| Key | Action |
|-----|--------|
| Space / ↑ | Jump |
| Enter | Restart after game over |
| Esc | Quit (from game-over screen) |

## Tech Stack
- Java
- Swing / AWT (rendering, input, timers)
- Java Sound API (`javax.sound.sampled`) for music and effects

## Project Structure
| Class | Purpose |
|-------|---------|
| `App` | Entry point; sets up the window and switches between screens |
| `StartScreen` | Title screen with the Start button |
| `KnightRunner` | Main game panel: game loop, input, collisions, rendering |
| `Knight` | Player character, physics, and animation states |
| `ObstacleManager` / `Obstacle` | Spawning, moving, and drawing obstacles |
| `Portal` | Special obstacle that triggers the ability wheel |
| `AbilityWheel` | Animated spinning wheel that picks a random power-up |
| `PowerUpManager` | Applies timed power-ups and on-screen flash effects |
| `ScoreCounter` | Score and high score tracking |
| `SoundManager` | Background music and sound effects |

## How to Run
Requires Java 8 or newer.

    git clone https://github.com/rishbabagi/KnightRunner.git
    cd KnightRunner/src
    javac *.java
    java App

## Possible Improvements
- Save the high score to a file so it carries over between sessions
- Add a crouch/slide move (the crouch animation is already included)
- More power-ups and obstacle types
