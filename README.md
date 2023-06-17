# Fantasy Ekstraklasa

Welcome to Fantasy Ekstraklasa project! The project allows to create a team of Ekstraklasa players, that will later score according to match events, that happen in “real world”. The project fetches players and teams data from API-FOOTBALL API and later fetches games data from LiveScore API.

The project generally uses 6 REST API Controllers, that allows to handle football players, football teams, users, squads and leagues. Lastly there is one service controller, that allows to fetch data “by hand”.

## Known issues

As of 17.06.23 LiveScore API decided to already switch to 2023 season and last season’s matches are no longer available - to fully use the functionality it is necessary to wait for 2023 season matches to start

API-FOOTBALL controller does not correctly return the list of players (not all players are available, there is one repetition) - possibly it is an issue connected with 2022 seasons ending and developers have already started work to switch to new season

## Service Controller

Service Controller is used to fetch data from APIs. It has two methods to fetch data about teams and players from API-FOOTBALL:

- `POST` - “_service/init/teams_” - this method allows to initialize teams from API
- `POST`- “_service/init/players_” - this method allows to initialize all players from API
- `PUT` - “_service/getScores_” - this method fetches match events for a current round and decorates players with score. It automatically **stores** and **increments** current round

It is necessary to successfully initiate teams and players to **first** initiate **teams** and **later players**. Also, fetching scores is ******************scheduled****************** and it is repeated every Monday at 9PM - this should be the end date of current round in Ekstraklasa.

## Player Controller

Player Controller is used primarily to return data about players list with a GET method. It returns *****Paged***** data with 20 players per page. It also allows for sorting players. Sorting options available are described below in the SortType data type description. All methods are:

- `GET` - “_players/page/{pageNo}/sortBy/{sortType}_” - returns a list of players on page pageNo and sorted by sortType
- `GET` - “_players/{id}_” - returns a player with a given id
- `PUT` - “_players_” - consumes PlayerDto JSON object to update player
- `DELETE` - “_players/{id}_” - deletes a player with a given id

## TeamController

Simillarly as player controller team controller is used primarily to return list of all teams. Apart from that it allows to update or delete a team.

- `GET` - “_teams_” - returns a list of all teams
- `GET` - “_teams/{id}_” - returns a team with a given id
- `PUT` - “_teams_” - consumes TeamDto JSON object to update team
- `DELETE` - “_teams/{id}_” - deletes a team with a given id

## UserController

User Controller allows for users handling all users need a name and email. Later users will be notified when the round is finished (scheduled to every Monday at 9PM). User can have a squad - which is a list of players, that user added to his squad and later will score for a given user. Users can also be added to Leagues. Available methods of UserController are:

- `GET` - “_users/{id}_” - returns a user with a given id
- `POST` - “_users_” - consumes CreateUserDto JSON to create a new user
- `PUT` - “_users_” - consumes UserDto JSON to update a given user
- `DELETE` - “_users/{id}_” - deletes a user with a given id
- `PUT` - “_users/{id}/createSquad/{squadName}_” - creates new squad with a given squad name

## SquadController

Squads are list of 11 (or less) players, that were chosen by a user to user’s team. Squad can have up to 11 players and whole team’s **value** (every player have a assigned value - value is assigned by rating, that is fetched from API-FOOTBALL) cannot be more than 30 000 000. SquadController allows for:

- `GET` - “_squads/{id}_” - get squad with a given id
- `PUT` - “_squads_” - consumes SquadDto JSON to update given squad
- `PUT` - “_squads/{squadId}/addPlayer/{playerId}_” - adds a player with a given id to a squad with a given id. If a squad is too big or too valuable it will throw a specific exception
- `PUT` -  “_squads/{squadId}/removePlayer/{playerId}_” - removes a player with a given id to a squad with a given id.

## LeagueController

Leagues are a list of users, that decided to enter a league. They can compete in such a league to see who picked players best and scored most points. League controller has mappings:

- `GET` - “_leagues_” - return all leagues
- `GET` - “_leagues/{id}_” - returns a league with a given id.
- `GET` - “_leagues/byUser/{userId}_” - returns a list of leagues, that user with a given id have entered
- `POST` - “_leagues/{leagueName}_” - creates a league with a given name
- `PUT` - “_leagues_” - consumes LeagueDto JSON object and updates a given league
- `DELETE` - “_leagues/{id}_” - deletes a league with a given id
- `PUT` - “_leagues/{leagueId}/addUser/{userId}_” - adds a user to a given league. If a user is already in this league - throws an exception
- `PUT` - “_leagues/{leagueId}/removeUSer/{userId}_“- removes a user from a given league.

### SortType

PlayersRepository allows for sorting as seen below:

1. `ID_ASCENDING` - sort by player id ascending
2. `ID_DESCENDING` - sort by player id descending
3. `LASTNAME_ASCENDING` - sort by player last name ascending
4. `LASTNAME_DESCENDING` - sort by player last name descending
5. `VALUE_ASCENDING` - sort by player value ascending
6. `VALUE_DESCENDING` - sort by player value descending
7. `TEAM_ASCENDING` - sort by player team ascending
8. `TEAM_DESCENDING` - sort by player team descending
