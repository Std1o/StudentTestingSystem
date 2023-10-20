package com.stdio.godofappstates.presentation.stateWrapper

/**
 * It is necessary for sending a callback from StateWrapper without sending the entire contentState
 */
interface OnReceiveListener {
    fun onReceive()
}