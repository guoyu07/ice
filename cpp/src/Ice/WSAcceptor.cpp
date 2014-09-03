// **********************************************************************
//
// Copyright (c) 2003-2014 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

#include <Ice/WSAcceptor.h>
#include <Ice/WSTransceiver.h>

using namespace std;
using namespace Ice;
using namespace IceInternal;

IceInternal::NativeInfoPtr
IceInternal::WSAcceptor::getNativeInfo()
{
    return _delegate->getNativeInfo();
}

#if defined(ICE_USE_IOCP)
IceInternal::AsyncInfo*
IceInternal::WSAcceptor::getAsyncInfo(IceInternal::SocketOperation status)
{
    return _delegate->getNativeInfo()->getAsyncInfo(status);
}
#elif defined(ICE_OS_WINRT)
void 
IceInternal::WSAcceptor::setCompletedHandler(IceInternal::SocketOperationCompletedHandler^ handler)
{
    _delegate->getNativeInfo()->setCompletedHandler(handler);
}
#endif

void
IceInternal::WSAcceptor::close()
{
    _delegate->close();
}

void
IceInternal::WSAcceptor::listen()
{
    _delegate->listen();
}

#if defined(ICE_USE_IOCP) || defined(ICE_OS_WINRT)
void
IceInternal::WSAcceptor::startAccept()
{
    _delegate->startAccept();
}

void
IceInternal::WSAcceptor::finishAccept()
{
    _delegate->finishAccept();
}
#endif

IceInternal::TransceiverPtr
IceInternal::WSAcceptor::accept()
{
    //
    // WebSocket handshaking is performed in TransceiverI::initialize, since
    // accept must not block.
    //
    return new WSTransceiver(_instance, _delegate->accept());
}

string
IceInternal::WSAcceptor::protocol() const
{
    return _delegate->protocol();
}

string
IceInternal::WSAcceptor::toString() const
{
    return _delegate->toString();
}

IceInternal::WSAcceptor::WSAcceptor(const ProtocolInstancePtr& instance, const IceInternal::AcceptorPtr& del) :
    _instance(instance), _delegate(del)
{
}

IceInternal::WSAcceptor::~WSAcceptor()
{
}