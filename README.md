# RSS Reader

## Overview
RSS Reader is a simple Java application that allows users to keep track of their favorite websites' latest updates via RSS feeds. The application enables users to add, remove, and view RSS feeds from a list of websites.

## Features
- **Add a Website**: Users can add a new website by providing its URL. The application will automatically fetch the website's title and RSS feed URL.
- **Remove a Website**: Users can remove a website from their tracking list.
- **Show Updates**: Users can view the most recent posts from their selected websites or all websites in their list.
- **Save Websites**: The application saves the list of websites and their corresponding RSS feed URLs to a file for persistence.

## How to Use
1. Run the application using your Java IDE or from the command line.
2. Choose an option from the menu:
   - `[1] Show Updates`: View the latest updates from the websites.
   - `[2] Add URL`: Add a new website to the list.
   - `[3] Remove URL`: Remove an existing website from the list.
   - `[4] Exit`: Save the changes and exit the application.
3. Follow the prompts to add or remove websites.

## Installation
To run the RSS Reader, you need to have Java installed on your system. Clone the repository and compile the source code using the following commands:

```bash
git clone https://github.com/your-username/rss-reader.git
cd rss-reader
javac RssReader.java
java RssReader
