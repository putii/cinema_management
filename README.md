# Cinema Management System
## Project Description

This project is a Cinema Management System developed as a part of a University project for a Java language course. The system is designed to simulate the operations of a real-world cinema.

The Cinema Management System allows you to:

- View all clients, movies, showings, bookings, and halls.
- Add new clients, movies, showings, and halls.
- Make and cancel bookings.
- View the seating arrangement for a particular showing.

The system is implemented in Java and uses a MySQL database for data storage. It uses Maven for dependency management and build automation.

The main class of the application is `Cinema.java`, which provides a text-based user interface for interacting with the system. The user can select various options from the menu to perform different operations.

The `Server.java` and `ClientEndpoint.java` classes implement a simple client-server architecture using sockets. The server listens for incoming connections from clients and handles their requests in separate threads.

The project also includes a `pom.xml` file for managing Maven dependencies and build settings.

Please refer to the "Requirements", "Installation", and "Building and Running the Application" sections for instructions on how to set up and run the project.

## Requirements

- Java 21
- Maven
## Installation

Instructions on how to install and get your project running.

### Install Java

If you're using Ubuntu or any other Debian-based distribution, you can install Java using the `apt` package manager. Here's how:

1. First, update the package index:

```bash
sudo apt update
```

2. Next, install Java. If you want to install Java 17, you can do so with the following command:

```bash
sudo apt install openjdk-21-jdk
```


3. After the installation finishes, you can confirm that the correct version is being used by running:

```bash
java -version
```

This command should output the version of Java that you just installed.

### Install Maven

Maven is a build automation tool used primarily for Java projects. Here's how you can install it on Ubuntu:

1. First, update the package index:

```bash
sudo apt update
```

2. Next, install Maven:

```bash
sudo apt install maven
```

3. Verify the installation by checking the Maven version:

```bash
mvn -version
```

This command should output the version of Maven that you just installed.

Please note that Maven requires Java to be installed, so make sure you have a compatible version of Java on your system before installing Maven.


### Clone the project 

```bash
git clone https://github.com/yourusername/your-repo-name.git
cd your-repo-name
```

### Building and Running the Application

This project uses Maven for dependency management and build automation. You can build and run the application using the following steps:

1. Open a terminal and navigate to the root directory of the project (the directory containing the `pom.xml` file).

2. Run the following command to build the project and install the dependencies:

```bash
mvn clean install
```

### How to establish mySQL connection
If using XAMPP for local development using MySQL database, follow these steps:
1. Start XAMPP Apache and MySQL modules
2. go to http://localhost/phpmyadmin in your browser
3. Create cinema mysql database using script cinema-management\src\main\resources\schema.sql
4. Go to "User accounts" tab
5. Create new user "cinema_user" with password "1234"
5. Go back to "User accounts" tab
6. Click "Edit privileges"
7. Then click "Database" and grant necessary privileges to the database cinema






## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
