# Warzone-Java-Command-Line-Game
This is a Java-based Command Line implementation of the popular "Risk" game, compatible with the rules, map files, and the command-line play of the “Warzone” version of Risk, which can be found at [Warzone](https://www.warzone.com/).

## Technology Used

- **Java 1.8**: The game is implemented in Java, utilizing the features of Java 8.

## Design Patterns

The implementation follows proper design patterns to ensure a robust and maintainable codebase:

- **Observer Pattern**: Used to establish a one-to-many dependency between objects, ensuring that changes to one object's state notify and update all its dependents.

- **MVC Architecture**: Employed the Model-View-Controller architectural pattern for a clear separation of concerns, making the codebase modular and easier to maintain.

- **Command Pattern**: Implemented the Command Pattern to encapsulate a request as an object, allowing parameterization of clients with different requests, queuing of requests, and logging of the actions.

- **Strategy Pattern**: Utilized the Strategy Pattern to define a family of algorithms, encapsulate each algorithm, and make them interchangeable. This allows the client to choose the appropriate strategy at runtime.

- **State Pattern**: Applied the State Pattern to allow an object to alter its behavior when its internal state changes. This pattern represents a state machine where transitions between states are explicit and defined by the State pattern.

## Tools Used

- **Eclipse**: The integrated development environment used for Java development.

- **Git**: Version control system for tracking changes in the source code during software development.

## How to Run

1. Clone the repository: `git clone https://github.com/Rhmarwaha/Warzone-Java-Command-Line-Game.git`
2. Open the project in Eclipse or your preferred Java IDE.
3. Build and run the project.

## Usage

1. Follow the command-line prompts to start and play the game.
2. Make strategic decisions and conquer territories to emerge victorious.

Feel free to contribute, report issues, or enhance the game further!

## License

This project is licensed under the [MIT License](LICENSE) - feel free to use, modify, and distribute the code as per the license terms.

Happy gaming! 🎲🌐