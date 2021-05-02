## Code Review

Reviewed by: Xiang Lu, u7224266

Reviewing code written by: Xinjie Wang, u7201825

Component:  [isMoveValid](https://gitlab.cecs.anu.edu.au/u7201825/comp1110-ass2-tue12f/-/blob/master/src/comp1110/ass2/Game.java#L356-478)

### Comments 

The best features of this code is to check the validity of move and return a boolean result. Moreover the
code also has good comments, and the program decomposition is appropriate. In addition, it observes java code conventions
which can be seen from the tips without highlighting. However, there are also some deficiencies in this code. For example,
in the if statement, this code repeatedly calls game.getCommon() and game.getPlayers(), which can be simplified. 
All in all, it's a great piece of code.
