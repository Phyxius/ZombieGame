# Zombie House Game
## Authors: Shea Polansky, Ari Rappaport, Mohammed Yousefi
This was a game I created as part of a 3-man team for CS351: Design of Large Programs. It was designed to showcase our ability to architect complex, interconnected programs and implement various algorithms such as lighting and random generation. 

## Objective
In each level, the player must navigate the house, using their limited vision to find and reach the end.

## Controls
* WASD to move
* Hold R to run at double speed
 * While running, the player detonates traps that he steps on, resulting in a game over.
* Hold P to pick up or place traps

## Architecture
I was mainly responsible for the program architecture; the system of `Entities` managed by an `EntityManager`, which handles callbacks, events and drawing, is a system I developed based on my own personal game development efforts. I found it to be a useful system, so I copied the design over for this project.