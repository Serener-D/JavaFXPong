## JavaFXPong

This is yet another Pong implementation using [JavaFX](https://openjfx.io/).

I wanted to make this without any tutorials, as a result the code is a bit of a procedural mess. I might refactor it in
the future when I get some experience with game development.

### How to run:

Execute the <code>./gradlew run</code> command.

### Known bugs:

- When the rectangle intersects the ball with its upper or bottom side, the ball gets few pixels inside the rectangle
  and the "pong" sound is played multiple times instead of one.