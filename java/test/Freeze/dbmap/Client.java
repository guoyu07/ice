// **********************************************************************
//
// Copyright (c) 2001
// ZeroC, Inc.
// Billerica, MA, USA
//
// All Rights Reserved.
//
// Ice is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License version 2 as published by
// the Free Software Foundation.
//
// **********************************************************************

import Freeze.*;

public class Client
{
    static class StressThread extends Thread
    {
        public void
        run()
        {
            try
            {
		for(int i = 0; i < 50; ++i)
		{
		    java.util.Set entrySet = _map.entrySet();
		    java.util.Iterator p = entrySet.iterator();
		    if(p.hasNext())
		    {
			Object o = p.next();
			test(entrySet.contains(o));
		    }
		}
	    }
	    catch(Exception ex)
	    {
		ex.printStackTrace();
		System.err.println(ex);
	    }
	}
	
	StressThread(java.util.Map m)
        {
	    _map = m;
	}

	private java.util.Map _map;
    }

    static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static void
    test(boolean b)
    {
        if(!b)
        {
            throw new RuntimeException();
        }
    }

    private static void
    populateDB(java.util.Map m)
	throws DBException
    {
	for(int j = 0; j < alphabet.length(); ++j)
	{
            m.put(new Byte((byte)alphabet.charAt(j)), new Integer(j));
	}
    }

    private static int
    run(String[] args, java.util.Map m, DB db)
	throws DBException
    {
	//
	// Populate the database with the alphabet.
	//
	populateDB(m);

	int j;

	System.out.print("  testing populate... ");
        System.out.flush();
	for(j = 0; j < alphabet.length(); ++j)
	{
	    Object value = m.get(new Byte((byte)alphabet.charAt(j)));
	    test(value != null);
	}
	test(m.get(new Byte((byte)'0')) == null);
	for(j = 0; j < alphabet.length(); ++j)
	{
	    test(m.containsKey(new Byte((byte)alphabet.charAt(j))));
	}
	test(!m.containsKey(new Byte((byte)'0')));
	for(j = 0; j < alphabet.length(); ++j)
	{
	    test(m.containsValue(new Integer(j)));
	}
	test(!m.containsValue(new Integer(-1)));
	test(m.size() == alphabet.length());
	test(!m.isEmpty());
	System.out.println("ok");

	System.out.print("  testing erase... ");
        System.out.flush();
	m.remove(new Byte((byte)'a'));
	m.remove(new Byte((byte)'b'));
	m.remove(new Byte((byte)'c'));
	for(j = 3; j < alphabet.length(); ++j)
	{
	    Object value = m.get(new Byte((byte)alphabet.charAt(j)));
	    test(value != null);
	}
	test(m.get(new Byte((byte)'a')) == null);
	test(m.get(new Byte((byte)'b')) == null);
	test(m.get(new Byte((byte)'c')) == null);
	System.out.println("ok");
	
	//
	// Re-populate.
	//
	populateDB(m);

	{
	    System.out.print("  testing keySet... ");
	    System.out.flush();
	    java.util.Set keys = m.keySet();
	    test(keys.size() == alphabet.length());
	    test(!keys.isEmpty());
	    java.util.Iterator p = keys.iterator();
	    while(p.hasNext())
	    {
		Object o = p.next();
		test(keys.contains(o));

		Byte b = (Byte)o;
		test(m.containsKey(b));
	    }
	    System.out.println("ok");
	}

	{
	    System.out.print("  testing values... ");
	    System.out.flush();
	    java.util.Collection values = m.values();
	    test(values.size() == alphabet.length());
	    test(!values.isEmpty());
	    java.util.Iterator p = values.iterator();
	    while(p.hasNext())
	    {
		Object o = p.next();
		test(values.contains(o));

		Integer i = (Integer)o;
		test(m.containsValue(i));
	    }
	    System.out.println("ok");
	}

	{
	    System.out.print("  testing entrySet... ");
	    System.out.flush();
	    java.util.Set entrySet = m.entrySet();
	    test(entrySet.size() == alphabet.length());
	    test(!entrySet.isEmpty());
	    java.util.Iterator p = entrySet.iterator();
	    while(p.hasNext())
	    {
		Object o = p.next();
		test(entrySet.contains(o));

		java.util.Map.Entry e = (java.util.Map.Entry)o;
		test(m.containsKey(e.getKey()));
		test(m.containsValue(e.getValue()));
	    }
	    System.out.println("ok");
	}

	{
	    System.out.print("  testing iterator.remove... ");
	    System.out.flush();

	    test(m.size() == 26);
	    test(m.get(new Byte((byte)'b')) != null);
	    test(m.get(new Byte((byte)'n')) != null);
	    test(m.get(new Byte((byte)'z')) != null);

	    java.util.Set entrySet = m.entrySet();
	    java.util.Iterator p = entrySet.iterator();
	    while(p.hasNext())
	    {
		Object o = p.next();
		java.util.Map.Entry e = (java.util.Map.Entry)o;
		Byte b = (Byte)e.getKey();
		byte v = b.byteValue();
		if(v == (byte)'b' || v == (byte)'n' || v == (byte)'z')
		{
		    p.remove();
		}
	    }

	    ((Freeze.Map.EntryIterator)p).close();
 
	    test(m.size() == 23);
	    test(m.get(new Byte((byte)'b')) == null);
	    test(m.get(new Byte((byte)'n')) == null);
	    test(m.get(new Byte((byte)'z')) == null);

	    //
	    // Re-populate.
	    //
	    populateDB(m);
	    
	    test(m.size() == 26);

	    entrySet = m.entrySet();
	    p = entrySet.iterator();
	    while(p.hasNext())
	    {
		Object o = p.next();
		java.util.Map.Entry e = (java.util.Map.Entry)o;
		byte v = ((Byte)e.getKey()).byteValue();
		if(v == (byte)'a' || v == (byte)'b' || v == (byte)'c')
		{
		    p.remove();
		}
	    }

	    test(m.size() == 23);
	    test(m.get(new Byte((byte)'a')) == null);
	    test(m.get(new Byte((byte)'b')) == null);
	    test(m.get(new Byte((byte)'c')) == null);
	    System.out.println("ok");
	}

	/**
	 *
	 * TODO: BENOIT: Re-enable this test. The stress test is disabled for now. It exposes 
	 * many self dead locks. I believe this is caused by how we setup the database environment,
	 * i.e.: with all the flags to enable Berkeley DB Transactional support. However since we 
	 * don't use transactions in the code this leads to deadlocks. If we specify DB_INIT_TXN we 
	 * should use transactions (http://www.sleepycat.com/docs/ref/transapp/env_open.html).
	 *
	 * There's also some details at http://www.sleepycat.com/docs/ref/lock/notxn.html.
	 *
	 * Two solutions:
	 *
	 *  - use transactions.
	 *  - use BerkeleyDB Concurrent Data Store product instead.
	 *
	{
	    System.out.print("  testing concurrent access... ");
	    System.out.flush();
	
	    java.util.List l = new java.util.ArrayList();
	
	    //
	    // Create each thread.
	    //
	    for(int i = 0; i < 10; ++i)
	    {
		if(m instanceof ByteIntMapXML)
		{
		    l.add(new StressThread(new ByteIntMapXML(db)));
		}
		else
		{
		    l.add(new StressThread(new ByteIntMapBinary(db)));
		}
	    }

	    //
	    // Start each thread.
	    //
	    java.util.Iterator p = l.iterator();
	    while(p.hasNext())
	    {
		Thread thr = (Thread)p.next();
		thr.start();
	    }
	
	    //
	    // Wait for each thread to terminate.
	    //
	    p = l.iterator();
	    while(p.hasNext())
	    {
		Thread thr = (Thread)p.next();
		while(thr.isAlive())
		{
		    try
		    {
			thr.join();
		    }
		    catch(InterruptedException e)
		    {
		    }
		}
	    }

	    System.out.println("ok");
	}
	*/

	return 0;
    }

    static public void
    main(String[] args)
    {
	int status;
	Ice.Communicator communicator = null;
	DBEnvironment dbEnv = null;
	String dbEnvDir = "db";
        DB xmlDB = null, binaryDB = null;

	try
	{
	    Ice.StringSeqHolder holder = new Ice.StringSeqHolder();
	    holder.value = args;
	    communicator = Ice.Util.initialize(holder);
	    args = holder.value;
	    if(args.length > 0)
	    {
		dbEnvDir = args[0];
		dbEnvDir += "/";
		dbEnvDir += "db";
	    }
	    dbEnv = Freeze.Util.initialize(communicator, dbEnvDir);
            xmlDB = dbEnv.openDB("xml", true);
            ByteIntMapXML xml = new ByteIntMapXML(xmlDB);
            System.out.println("testing XML encoding...");
            status = run(args, xml, xmlDB);
            if(status == 0)
            {
                binaryDB = dbEnv.openDB("binary", true);
                ByteIntMapBinary binary = new ByteIntMapBinary(binaryDB);
                System.out.println("testing binary encoding...");
                status = run(args, binary, binaryDB);
            }
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	    System.err.println(ex);
	    status = 1;
	}

        if(xmlDB != null)
        {
            try
            {
                xmlDB.close();
            }
            catch(DBException ex)
            {
                System.err.println(args[0] + ": " + ex + ": " + ex.message);
                status = 1;
            }
            catch(Ice.LocalException ex)
            {
                System.err.println(args[0] + ": " + ex);
                status = 1;
            }
            catch(Exception ex)
            {
                System.err.println(args[0] + ": unknown exception: " + ex);
                status = 1;
            }
            xmlDB = null;
        }

        if(binaryDB != null)
        {
            try
            {
                binaryDB.close();
            }
            catch(DBException ex)
            {
                System.err.println(args[0] + ": " + ex + ": " + ex.message);
                status = 1;
            }
            catch(Ice.LocalException ex)
            {
                System.err.println(args[0] + ": " + ex);
                status = 1;
            }
            catch(Exception ex)
            {
                System.err.println(args[0] + ": unknown exception: " + ex);
                status = 1;
            }
            binaryDB = null;
        }

	if(dbEnv != null)
	{
	    try
	    {
		dbEnv.close();
	    }
	    catch(DBException ex)
	    {
		System.err.println(args[0] + ": " + ex + ": " + ex.message);
		status = 1;
	    }
	    catch(Ice.LocalException ex)
	    {
		System.err.println(args[0] + ": " + ex);
		status = 1;
	    }
	    catch(Exception ex)
	    {
		System.err.println(args[0] + ": unknown exception: " + ex);
		status = 1;
	    }
	    dbEnv = null;
	}

	if(communicator != null)
	{
	    try
	    {
		communicator.destroy();
	    }
	    catch(Exception ex)
	    {
		System.err.println(ex);
		status = 1;
	    }
	}

	System.exit(status);
    }
}
