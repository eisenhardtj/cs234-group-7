# cs234-group-7
Intro to Software Engineering Project 

# Contributors

Jeffery Eisenhardt - eisenhardtj

Christine Colvin - christinecolvin

Cole Aydelotte - coleaydelotte

Jalil Rodriguez - JalilR08

# Directions

## Step 1

Download and install MySQL for your MacOS by following the instructions:
https://dev.mysql.com/doc/refman/8.3/en/macos-installation-pkg.html

If you have HomeBrew installed you can paste the following command into your terminal:
```shell
Brew install mysql
```

## Step 2
Open the terminal and enter the following command once MySQL is downloaded:
```shell
mysql -u root -p
```
You will be prompted for a password but, since this is the first time you have logged in their 
is not one set so you can just press the return key to bypass.

If you have had MySQL downloaded before then you might have set a root password, please enter
this password when prompted.

## Step 3
Once logged in you can now paste the following command in the terminal:
```sql
CREATE USER 'project'@'localhost' IDENTIFIED BY 'project';
```
then enter the following command:
```sql
GRANT ALL PRIVILEGES ON *.* TO 'project'@'localhost' WITH GRANT OPTION;
```
after you can enter quit into the terminal.
```sql
quit
```
## Step 4
Clone the repository into a preferred location using the command:
```shell
git clone https://github.com/eisenhardtj/cs234-group-7.git
```

## Step 5

Open your terminal, navigate to the path where you cloned the repository, 
then run the command:
```shell
mysql -u project -p < dumpfiles/dump.sql
```
You will be prompted to enter a password--enter 'project' case sensitive.

## Step 6

### Opening program without jar executable
After opening the repository, you have to run BasketballTeamRosterGUI.java file to run the program.

### Opening program with Roster.jar

Find the Roster.jar file in the project root, and double click the file to run the program.
