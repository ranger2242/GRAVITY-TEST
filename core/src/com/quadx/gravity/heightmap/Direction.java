package com.quadx.gravity.heightmap;

import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Jonas on 1/12/2017.
 */
public class Direction {
    public enum Facing{
        North,Northwest,West,Southwest,South,Southeast,East,Northeast;
        private static final List<Facing> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Facing getRandom()  {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }
    public static Facing getDirection(double angle){
        int x= (int) Math.round(Math.cos(angle));
        int y= (int) Math.round(Math.sin(angle));
        Facing f=Facing.South;
        switch (x){
            case -1:{
                switch (y){
                    case -1:{
                        f= Facing.Southwest;
                        break;
                    }
                    case 0:{
                        f= Facing.West;
                        break;
                    }
                    case 1:{
                        f= Facing.Northwest;
                        break;
                    }
                }
                break;
            }
            case 0:{
                switch (y){
                    case -1:{
                        f= Facing.South;
                        break;
                    }
                    case 0:{
                        f= Facing.South;
                        break;
                    }
                    case 1:{
                        f= Facing.North;
                        break;
                    }
                }
                break;
            }
            case 1:{
                switch (y){
                    case -1:{
                        f= Facing.Southeast;
                        break;
                    }
                    case 0:{
                        f= Facing.East;
                        break;
                    }
                    case 1:{
                        f= Facing.Northeast;
                        break;
                    }
                }
                break;
            }
            default:f= Facing.South;
        }
        return f;
    }
    public static Vector2 getVector(Facing facing){
        switch (facing){

            case North: {
                return new Vector2(0,1);
            }
            case Northwest:{
                return new Vector2(-1,1);
            }
            case West:{
                return new Vector2(-1,0);
            }
            case Southwest:{
                return new Vector2(-1,-1);
            }
            case South:{
                return new Vector2(0,-1);
            }
            case Southeast:{
                return new Vector2(1,-1);
            }
            case East:{
                return new Vector2(1,0);
            }
            case Northeast:{
                return new Vector2(1,1);
            }
            default:return null;
        }
    }
}
