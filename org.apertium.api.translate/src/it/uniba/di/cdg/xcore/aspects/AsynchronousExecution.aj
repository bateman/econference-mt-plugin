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

/**
 * Base aspect for implementing asynchronous execution. This is a policy which executes operation
 * in a different, new thread: since the join points can be captured in different ways, implementors
 * are requested to define the <code>asyncOperations</code> pointcut.
 * 
 * This code is the <b>Worker Pattern</b> described in <i>AspectJ in action</i> book (section 8.1).
 */
public abstract aspect AsynchronousExecution {
    /**
     * Left to implementations: all captured joinpoints will be run in a new thread.
     */
    protected abstract pointcut asyncOperations();
    
    void around() : asyncOperations() {
        Runnable worker = new Runnable() {
            public void run() {
                proceed();
            }
        };
        Thread t = new Thread( worker );
        t.start();
    }
}
