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

package IceInternal;

public interface Endpoint extends java.lang.Comparable
{
    //
    // Marshal the endpoint
    //
    void streamWrite(BasicStream s);

    //
    // Convert the endpoint to its string form
    //
    String toString();

    //
    // Return the endpoint type
    //
    short type();

    //
    // Return the timeout for the endpoint in milliseconds. 0 means
    // non-blocking, -1 means no timeout.
    //
    int timeout();

    //
    // Return a new endpoint with a different timeout value, provided
    // that timeouts are supported by the endpoint. Otherwise the same
    // endpoint is returned.
    //
    Endpoint timeout(int t);

    //
    // Return true if the endpoint is datagram-based.
    //
    boolean datagram();

    //
    // Return true if the endpoint is secure.
    //
    boolean secure();

    //
    // Return true if the endpoint type is unknown.
    //
    boolean unknown();

    //
    // Return a client side transceiver for this endpoint, or null if a
    // transceiver can only be created by a connector.
    //
    Transceiver clientTransceiver();

    //
    // Return a server side transceiver for this endpoint, or null if a
    // transceiver can only be created by an acceptor. In case a
    // transceiver is created, this operation also returns a new
    // "effective" endpoint, which might differ from this endpoint,
    // for example, if a dynamic port number is assigned.
    //
    Transceiver serverTransceiver(EndpointHolder endpoint);

    //
    // Return a connector for this endpoint, or null if no connector
    // is available.
    //
    Connector connector();

    //
    // Return an acceptor for this endpoint, or null if no acceptors
    // is available. In case an acceptor is created, this operation
    // also returns a new "effective" endpoint, which might differ
    // from this endpoint, for example, if a dynamic port number is
    // assigned.
    //
    Acceptor acceptor(EndpointHolder endpoint);

    //
    // Check whether the endpoint is equivalent to a specific
    // Transceiver or Acceptor
    //
    boolean equivalent(Transceiver transceiver);
    boolean equivalent(Acceptor acceptor);

    //
    // Compare endpoints for sorting purposes
    //
    boolean equals(java.lang.Object obj);
    int compareTo(java.lang.Object obj); // From java.lang.Comparable
}
