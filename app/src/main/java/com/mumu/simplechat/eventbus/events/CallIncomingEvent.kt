package com.mumu.simplechat.eventbus.events

import com.mumu.simplechat.model.ICallModel

class CallIncomingEvent(val from: String, val type: ICallModel.CallType)