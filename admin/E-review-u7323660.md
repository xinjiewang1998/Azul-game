## Code Review

Reviewed by: Jiaan Guo, u7323660

Reviewing code written by: Xiang Lu, u7224266

Component: Game.isStateValid() (Task 9 Line 300 to Line 354)
            [Game.isStateValid](https://gitlab.cecs.anu.edu.au/u7201825/comp1110-ass2-tue12f/-/blob/master/src/comp1110/ass2/Game.java#L300-354)

### Comments 
The code has clear structure and meaningful comments, the program decomposition is appropriate: 
counting tiles are well delegated to each class, methods and variables are named properly and 
consistently, but the code still exists some magic number like 5 and 20, which could potentially 
make extensions harder, better use some static constants defined above instead.
