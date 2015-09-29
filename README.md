##Poughkeepsie hold'em
This is a very simple poker game of just one card and a deck of 10 cards, 2-Ace.
However, this simple poker has
interesting and surprisingly complex features that teach much about
poker, probability theory, machine learning, and artificial intelligence.

The idea for the project was initially found in Baker, R.J.S. and Cowling, P.I.,
"Bayesian Opponent Modeling in a Simple Poker Environment,"
*Computational Intelligence and Games, 2007. CIG 2007. IEEE Symposium on*.
However, since I could not find their source code, I decided to develop my
own.
Aside from the main concepts which I developed in Java,
I also fixed a few problems or perhaps just clarified them mainly around the
matter of win probabilities upon which the entire concept
depends--and in my opinion, more than application of Bayesian opponent modeling.
*Pok hold'em* is the result which I created for teaching purposes.

Pok hold'em supports five (5) player styles, including the Bayesian opponent 
model from Baker and Cowling (2007) and a human
player interface for testing, debugging, and fun.

To play Pok hold'em, clone the project, open in the latest release of NetBeans,
and run the *Game* file
in the poker package.
The system is
configured by default with two players, one of them the human command line interface.
To change the configuration parameter, modify the pokpoker.json file.

##Some TODOs
1. Add a GUI.
2. Add win probability matrix for player folding and revealing card.
3. Extend the game for more than one card to make it even more interesting.
4. Add money management as the bluffing aggressive player will drive others to the bankruptcy.
4. Test more.
