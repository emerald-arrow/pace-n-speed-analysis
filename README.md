# Pace N Speed Analysis

It is a tool for basic analysis of top speed and lap times from endurance racing sessions. It supports analysis.csv
files from:

- [FIA WEC](https://fiawec.alkamelsystems.com)
- [IMSA sanctioned series](https://imsa.results.alkamelcloud.com/)
- [ELMS](https://elms.alkamelsystems.com)
- [AsLMS](https://alms.alkamelsystems.com)
- [Le Mans Cup](https://lemanscup.alkamelsystems.com)

# Motivation
The goal of this tool is to provide a way to analyse endurance racing lap data without relying on spreadsheets.

# Features
- Analysis of endurance racing lap times and top speeds
- Automatic detection and removal of outliers
- Multiple filtering options (pit laps, first laps, slow laps, periods, etc.)
- Aggregation by drivers, manufacturers or teams
- Export to CSV and R data frame
- English and Polish UI with appropriate locale used in data (commas etc.)

# Running the application
Requirements:
- Java JDK 25
- Apache Maven 3.9.16

Open project's root directory in the terminal and build the JAR in the using command:
```
mvn clean package
```
And then run it by typing:
```
java -jar target/pace-n-speed-analysis-1.0.jar
```

# Usage
## Step 0 - Download an analysis file
First download an analysis.csv file from one of the aforementioned websites. It must have the following columns for all
types of analysis:
- "NUMBER" with team's car number
- "LAP_NUMBER" with lap's number
- "CROSSING_FINISH_LINE_IN_PIT" with indication whether it was in lap or not
-  "DRIVER_NAME" with driver's name
- "CLASS" with category's name
- "TEAM" with team's name
- "MANUFACTURER" with manufacturer's name
- "FLAG_AT_FL" with data about the flag that was at the end of the lap
- "ELAPSED" with time when the lap was set

For pace analysis the following columns are required:
- "LAP_TIME" with lap's time
- "S1" with lap's sector 1 time
- "S2" with lap's sector 2 time
- "S3" with lap's sector 3 time

For speed analysis the required column is:
- "TOP_SPEED" with the speed at speed trap

## Step 1 - Choosing file and analysis type
Upload the file you had downloaded in the app, then choose the type of the analysis: top speed or lap times. Below that
you can pick aggregation: by drivers, by teams (entrants) or by manufacturers.

## Step 2 - Choosing category
In the next step choose which category you would like to analyse. After making the choice the app automatically
calculates outliers and hides them from the analysis. 

## Step 3 - Filtering data
Third step is filtering out unwanted laps, available options are:
- filtering first laps - the first lap of the race, in case of drivers: their first laps (either of the entire race or
their first out laps), in case of manufacturers: the first laps of the race of all teams representing them, in case of
teams: the first laps of the entire race
- filtering pit laps - in laps and/or out laps to/from the pit lane
- filtering non-green flags at the finish line - laps that have non-green flag at the finish line - Red Flag, Full
Course Yellow and so on including unrecognised, unknown flags
- filtering slow laps - laps that are slower than entered per cent
    - in case of lap times just lap times can be used or both lap times and sector times, example: 5 per cent chosen as
filtering value means times slower than 105% of each driver's/manufacturer's/team's best are going to be removed
    - in case of top speeds it is basic removal based on being slower than the best top speed, example: 5 per cent
chosen as filtering value means that top speeds slower than 95% of each driver's/manufacturer's/team's best are going
to be removed
- Picking best laps (by number) - outright choosing the fastest laps by entered number, example: 25 as the entered
value means that each driver's/manufacturer's/team's best 25 laps are going to be preserved, if they do not have 25
laps under their belts then they are going to be omitted from the analysis
- Picking best laps by percentage - outright choosing the fastest laps but as a percentage of each
driver's/manufacturer's/team's total lap count, example: 60% means that each total lap count is multiplied by 0.6 and
rounded up, if a driver/team/manufacturer had covered 397 laps the analysis is going to take 239 laps into account
- excluding periods - all laps set between start and end of provided periods are going to be filtered out, laps that
fell within the end of the provided time periods also are going to be filtered out
- picking period - only laps set between the start and the end of the provided period are going to be preserved,
there are not any checks regarding "how much" of the lap was set inside and outside that period

## Step 4 - Analysis view
It features a table that consists of drivers'/manufacturers'/teams' names, averages, medians, standard deviations, laps
taken into the analysis and total laps counts. There are options to start over with a new file and to export the
analysis to R data frame and CSV format. R data frames exports have the following characteristics: pace data is in
milliseconds and speed data is as floating-point numbers.

# Limitations
The tool does not:
- support splitting data by stints
- allow to manually pick laps to be taken into the analysis
- support Formula E analysis files, despite analysis files having similar format

# Screenshots
An example analysis with English locale
![example analysis EN](/docs/example_analysis_en.png)
The same analysis with Polish locale
![example analysis PL](/docs/example_analysis_pl.png)

# Tech stack
- Java 25 - core language
- Spring Boot 4 - application framework
- Vaadin 25 - GUI framework
- OpenCSV 5 - reading .CSV files

# Contributing
This is a personal project and contributions are not expected.