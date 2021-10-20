# Connect X

## Overview
## Basic Features
The features described in this section are:  
-	the option to start a new game  
-	variable game settings 
-	input validation 
-	automatic winner detection.

### Starting a new game
After the current game ends, the user is prompted to choose if he or she wants to start a new game. This is done by pressing ‘y’ (yes) or ‘n’ (no) (and Enter).

### Variable game settings
The user can choose to play using the default board dimensions (a board of 6 x 7) or using other dimensions which have to be natural numbers from the interval [1, 100]. 

The user can also choose the Connect type: the default Connect Four or another type of Connect X. After these choices are made, we have to verify that the combination of these values is valid, namely if the game can be won horizontally, vertically or diagonally. It is enough if the following condition is satisfied.

### Input validation
We identify two categories of possible invalid input: 
-	input that is always invalid – e.g. the user typed ‘w’ for a ‘y’/‘n’ prompt 
-	input which is invalid in the context of previous choices – e.g. the user chose a type of Connect x incompatible with the board dimensions. 

These two categories are dealt with separately.


## Advanced Features 
The features described in this section are: 
-	play against the computer 
-	save and load the game 
-	bullet mode 

### Play against the Computer
The user can choose at the start of each game the type of the opponent to play against: human or NPC and, depending on the chosen game settings (connect type, board dimensions), also the difficulty.

### Save and Load the game
Before each game, the user has the option to start a new game or to resume an old game. If there is no saved old game, the user will be informed and a new game will start automatically. If the user wishes to concede the game, they will be asked if the game (in the current state) should be saved or not. The save/load option is available for both game modes (Normal and Bullet – described below).

### Bullet mode
We have introduced another game mode: Bullet Mode (inspired from blitz chess). Each player has a total of 60 seconds at the start of the game. If one of the players runs out of time, the other player wins. The user can select the mode at the begging of each game.



## What's next
During the evaluation of the final program, a few more possible minor improvements were detected: 
-	the medium NPC could be improved by checking that the move chosen by the computer is not creating a winning configuration for the opponent 
-	the bullet mode could be adapted when the user plays against the computer (most of the times, the NPC finishes with 60 seconds left) 
-	more instructions could be given to the user – e.g. the moves are indexed from 1 (not 0) 

