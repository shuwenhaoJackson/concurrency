package com.mmall.concurrency.example.lock;


import java.util.concurrent.locks.StampedLock;

public class LockExample4 {

    class Point{
        private double x, y;
        private final StampedLock s1 = new StampedLock();

        void move(double deltaX, double deltaY){// an exclusively locked method
            long stamp = s1.writeLock();
            try {
                x += deltaX;
                y += deltaY;
            }finally {
                s1.unlockWrite(stamp);
            }
        }

        //乐观锁案例
        double distanceFromOrigin(){ // A read-only method
            long stamp = s1.tryOptimisticRead(); //获取一个乐观读锁
            double currentX = x, currentY = y; //将两个字段读入本地局部变量
            if (!s1.validate(stamp)) { //检查发出乐观读锁后同时是否有其他写锁发生？
                stamp = s1.readLock();
                try {
                    currentX = x; //将两个字段写入本地局部变量
                    currentY = y; //将两个字段写入本地局部变量
                }finally {
                    s1.unlockRead(stamp);
                }
            }
            return Math.sqrt(currentX * currentX + currentY *currentY);
        }

        //悲观锁案例
        void moveIfAtOrigin(double newX, double newY){ // upgrad
            //Could instead start with optimistic, not read mode
            long stamp = s1.readLock();
            try {
                while (x == 0.0 && y==0.0){ //循环，检查当前状态是否符合
                    long ws = s1.tryConvertToWriteLock(stamp); //将读锁转为写锁
                    if (ws != 0L) { // 这里确认写锁是否成功
                        stamp = ws; //如果成功，替换票据
                        x = newX; //进行状态改变
                        y = newY; //进行状态改变
                        break;
                    }else { //如果不能成功转换为写锁
                        s1.unlockRead(stamp); //我们显示释放锁
                        stamp = s1.writeLock(); //显示直接进行写锁，然后再通过循环再试
                    }
                }
            }finally {
                s1.unlock(stamp); //释放读锁或写锁
            }

        }
    }
}
