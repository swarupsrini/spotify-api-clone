# Spotify API Clone

A REST API for a Spotify-like music application. Supports two systems: profiles and songs. For the profile system, apps can call the API to create, remove, profiles and friend, follow other profiles. Apps can also like/unlike songs to add/remove it from their playlist. For the song system, apps can create, delete songs and query for songs by properties. The API uses 2 microservices to handle the 2 systems, providing extensibility to the API.

## Tech

`Java`, `Spring Boot`, `MongoDB`, `Neo4j`

## Motives

I'm a keen user of Spotify. I've always wondered how the application works behind the scenes, and how it manages so many songs and users concurrently. In addition, Spotify being a popular application has many feature updates and I wondered how they make these updates while making sure not to break any code. So I learned about the power of microservices that achieves this very task and created this project to demonstrate my knowledge.

## Development

Developed in a team of 2. Utilizes the Java Spring Boot framework to create the API and the microservices. The MongoDB document database is used for the profiles and the Neo4j graph database is used for the songs and the mapping from profiles to songs (i.e. which profile has which song in their playlist).

## Demo

The MongoDB database:  
![](https://i.imgur.com/NeyUZ0Y.jpg)

The Neo4j database:  
![](https://i.imgur.com/oalud9B.jpg)
