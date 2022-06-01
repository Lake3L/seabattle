package com.example.seabattle;

public class Game {
    PositionAndMove field;

    public Game(PositionAndMove field){this.field = field;}
    public Game(){
        field = new PositionAndMove();
        field.setField(true);
        field.setField(false);
    }


//      Make player step.
//      @param ip i coordinate of cell(row)
//      @param jp j coordinate of cell(column)
//      @return true if player hit any ship,
//      else false.

    public boolean attackEnemy(int ip, int jp){
        return field.hitEnemy(ip,jp);
    }

    public boolean attackPlayer(){
        return field.hitPlayer();
    }


    //check for finish game already
    int getWinner(){
        int[][] player = field.getPlayer();
        int[][] enemy = field.getEnemy();
        int winner = 1;
        for(int i = 0; i <field.N;++i)
            for(int j = 0; j < field.N;++j) {
                //even one part of ship is still not attacked
                if (enemy[i][j] == field.SHIP)
                    winner = -1;
                System.out.println("Nobody won");
            }
        if(winner == 1)
            return  winner;
        System.out.println("You won!");
        winner = 2;
        System.out.println("You lose :(");
        for(int i = 0; i <field.N;++i)
            for(int j = 0; j <field.N;++j) {
                //even one part of ship is still not attacked
                if (player[i][j] == field.SHIP)
                    winner = -1;
            }
        return winner;
    }

    public int[][] getPlayerPlacement(){
        return field.getPlayer();
    }
    public int[][] getEnemyPlacement(){
        return field.getEnemy();
    }
}
