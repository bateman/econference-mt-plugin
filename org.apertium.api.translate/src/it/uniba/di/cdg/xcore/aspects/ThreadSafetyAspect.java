/**
 * This file is part of the eConference project and it is distributed under the 
 * terms of the MIT Open Source license.
 * 
 * The MIT License
 * Copyright (c) 2005 Collaborative Development Group - Dipartimento di Informatica, 
 *                    University of Bari, http://cdg.di.uniba.it
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package it.uniba.di.cdg.xcore.aspects;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Reusable aspect for implementing the thread safety through a mutex.
 */
@Aspect( "perthis( readOperations() || writeOperations() )" )
public abstract class ThreadSafetyAspect {
    /**
     * Defines the read operations (operations that do not change the locked stuff). 
     */
    @Pointcut( "" )
    protected abstract void readOperations();
    
    /**
     * Defines the write operations (operations that do change the locked stuff). 
     */
    @Pointcut( "" )
    protected abstract void writeOperations();
    
    private ReadWriteLock _lock = new ReentrantReadWriteLock();
    
    @Before( "readOperations()" )
    public void beforeRead() {
        _lock.readLock().lock();
    }

    @After( "readOperations()" )
    public void afterRead() {
        _lock.readLock().unlock();
    }

    @Before( "writeOperations()" )
    public void beforeWrite() {
        _lock.writeLock().lock();
    }

    @After( "writeOperations()" )
    public void afterWrite() {
        _lock.writeLock().unlock();
    }
}
