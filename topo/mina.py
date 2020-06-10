#!/usr/bin/python

from mininet.node import Controller, OVSKernelSwitch
from mininet.log import setLogLevel, info
from mn_wifi.net import Mininet_wifi
from mn_wifi.node import Station, OVSKernelAP
from mn_wifi.cli import CLI
from mn_wifi.link import wmediumd, adhoc
from mn_wifi.wmediumdConnector import interference
from subprocess import call

from mininet.log import output, error
from mininet.term import makeTerms


import threading
import inspect
import ctypes


def myNetwork():

    net = Mininet_wifi(topo=None,
                       build=False,
                       link=wmediumd,
                       wmediumd_mode=interference,
                       ipBase='10.0.0.0/24')

    info( '*** Adding controller\n' )
    c0 = net.addController(name='c0',
                           controller=Controller,
                           protocol='tcp',
                           port=6633)

    info( '*** Add switches/APs\n')
    s1 = net.addSwitch('s1', cls=OVSKernelSwitch)
    s2 = net.addSwitch('s2', cls=OVSKernelSwitch)
    s3 = net.addSwitch('s3', cls=OVSKernelSwitch)
    s4 = net.addSwitch('s4', cls=OVSKernelSwitch)
    s5 = net.addSwitch('s5', cls=OVSKernelSwitch)
    s6 = net.addSwitch('s6', cls=OVSKernelSwitch)
    s7 = net.addSwitch('s7', cls=OVSKernelSwitch)
    s8 = net.addSwitch('s8', cls=OVSKernelSwitch)
    s9 = net.addSwitch('s9', cls=OVSKernelSwitch)
    s10 = net.addSwitch('s10', cls=OVSKernelSwitch)
    s11 = net.addSwitch('s11', cls=OVSKernelSwitch)
    s12 = net.addSwitch('s12', cls=OVSKernelSwitch)

    info( '*** Add hosts/stations\n')
    sta1 = net.addStation('sta1', ip='10.0.0.1',
                           position='1000,1000,0')
    sta2 = net.addStation('sta2', ip='10.0.0.2',
                           position='500,1000,0')
    sta3 = net.addStation('sta3', ip='10.0.0.3',
                           position='1500,1000,0')
    sta4 = net.addStation('sta4', ip='10.0.0.4',
                           position='1250,1250,0')
    sta5 = net.addStation('sta5', ip='10.0.0.5',
                           position='750,750,0')
    sta6 = net.addStation('sta6', ip='10.0.0.6',
                           position='750,1250,0')
    sta7 = net.addStation('sta7', ip='10.0.0.7',
                           position='1250,750,0')
    sta8 = net.addStation('sta8', ip='10.0.0.8',
                           position='375,1500,0')
    sta9 = net.addStation('sta9', ip='10.0.0.9',
                           position='900,1500,0')
    sta10 = net.addStation('sta10', ip='10.0.0.10',
                           position='650,1500,0')
    sta11 = net.addStation('sta11', ip='10.0.0.11',
                           position='1500,1250,0')
    sta12 = net.addStation('sta12', ip='10.0.0.12',
                           position='1500,750,0')
    sta13 = net.addStation('sta13', ip='10.0.0.13',
                           position='1150,1500,0')
    sta14 = net.addStation('sta14', ip='10.0.0.14',
                           position='1875,1550,0')
    sta15 = net.addStation('sta15', ip='10.0.0.15',
                           position='1420,1400,0')
    sta16 = net.addStation('sta16', ip='10.0.0.16',
                           position='1600,550,0')
    sta17 = net.addStation('sta17', ip='10.0.0.17',
                           position='1750,1320,0')
    sta18 = net.addStation('sta18', ip='10.0.0.18',
                           position='820,500,0')
    sta19 = net.addStation('sta19', ip='10.0.0.19',
                           position='1150,500,0')
    sta20 = net.addStation('sta20', ip='10.0.0.20',
                           position='1650,1550,0')
    sta21 = net.addStation('sta21', ip='10.0.0.21',
                           position='1675,1000,0')
    sta22 = net.addStation('sta22', ip='10.0.0.22',
                           position='1320,470,0')
    sta23 = net.addStation('sta23', ip='10.0.0.23',
                           position='210,460,0')
    sta24 = net.addStation('sta24', ip='10.0.0.24',
                           position='1820,750,0')
    sta25 = net.addStation('sta25', ip='10.0.0.25',
                           position='250,1000,0')
    sta26 = net.addStation('sta26', ip='10.0.0.26',
                           position='500,575,0')
    sta27 = net.addStation('sta27', ip='10.0.0.27',
                           position='150,750,0')
    sta28 = net.addStation('sta28', ip='10.0.0.28',
                           position='415,725,0')
    sta29 = net.addStation('sta29', ip='10.0.0.29',
                           position='200,1300,0')

    net.plotGraph(max_x=2000, max_y=2000)

    info("*** Configuring Propagation Model\n")
    # TODO : modify propagation Model "exp"
    # ref : mininet-wifi web or mininet-wifi/example/propagationModel
    net.setPropagationModel(model="logDistance", exp=3)

    info("*** Configuring wifi nodes\n")
    net.configureWifiNodes()

    info( '*** Add links\n')
    net.addLink(sta1, s1)
    net.addLink(s1, sta2)
    net.addLink(sta1, s2)
    net.addLink(s2, sta3)
    net.addLink(sta1, s3)
    net.addLink(s3, sta4)
    net.addLink(sta1, s4)
    net.addLink(s4, sta5)
    net.addLink(sta1, s5)
    net.addLink(s5, sta6)
    net.addLink(sta1, s6)
    net.addLink(s6, sta7)
    net.addLink(sta6, s7)
    net.addLink(s7, sta2)
    net.addLink(sta2, s12)
    net.addLink(s12, sta5)
    net.addLink(sta5, s11)
    net.addLink(s11, sta7)
    net.addLink(sta7, s10)
    net.addLink(s10, sta3)
    net.addLink(sta3, s9)
    net.addLink(s9, sta4)
    net.addLink(sta4, s8)
    net.addLink(s8, sta6)
    # TODO: add more 
    net.addLink(sta2, cls=adhoc, intf='sta2-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta3, cls=adhoc, intf='sta3-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta4, cls=adhoc, intf='sta4-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta5, cls=adhoc, intf='sta5-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta6, cls=adhoc, intf='sta6-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta7, cls=adhoc, intf='sta7-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta8, cls=adhoc, intf='sta8-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta9, cls=adhoc, intf='sta9-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta10, cls=adhoc, intf='sta10-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta11, cls=adhoc, intf='sta11-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta12, cls=adhoc, intf='sta12-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta13, cls=adhoc, intf='sta13-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta14, cls=adhoc, intf='sta14-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta15, cls=adhoc, intf='sta15-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta16, cls=adhoc, intf='sta16-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta16, cls=adhoc, intf='sta16-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta17, cls=adhoc, intf='sta17-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta18, cls=adhoc, intf='sta18-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta18, cls=adhoc, intf='sta18-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta19, cls=adhoc, intf='sta19-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta20, cls=adhoc, intf='sta20-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta21, cls=adhoc, intf='sta21-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta22, cls=adhoc, intf='sta22-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta23, cls=adhoc, intf='sta23-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta24, cls=adhoc, intf='sta24-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta25, cls=adhoc, intf='sta25-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta25, cls=adhoc, intf='sta25-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta26, cls=adhoc, intf='sta26-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta27, cls=adhoc, intf='sta27-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta28, cls=adhoc, intf='sta28-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta29, cls=adhoc, intf='sta29-wlan0',
                ssid='adhocNet', mode='g', channel=5)


    info( '*** Starting network\n')
    net.build()
    info( '*** Starting controllers\n')
    for controller in net.controllers:
        controller.start()

    info( '*** Starting switches/APs\n')
    net.get('s1').start([])
    net.get('s2').start([])
    net.get('s3').start([])
    net.get('s4').start([])
    net.get('s5').start([])
    net.get('s6').start([])
    net.get('s7').start([])
    net.get('s8').start([])
    net.get('s9').start([])
    net.get('s10').start([])
    net.get('s11').start([])
    net.get('s12').start([])

    info( '*** Post configure nodes\n')

    CLI.do_openMultiXterm = openMultiXterm
    CLI.do_execute = execute
    CLI.do_stopThread = stopThread
    CLI.do_stopTest = stopTest
    CLI(net)
    
    net.stop()

# add by u284976
def openMultiXterm(self, line):
    term = 'xterm'
    args = line.split()
    if not args:
        error( 'usage: openMultiXterm m n ...\n' )
    else:
        for i in range(int(args[0]),int(args[1])+1):
            arg = 'sta' + str(i)
            if arg not in self.mn:
                error( "node '%s' not in network\n" % arg )
            else:
                node = self.mn[ arg ]
                self.mn.terms += makeTerms( [ node ], term = term )

Threads = []

def execute(self, line):
    args = line.split()
    # please enter 2 at args[0]
    for i in range(int(args[0]),int(args[1])+1):
        node = self.mn['sta'+str(i)]
        node.cmd("cd ramp/" + str(i) + "-node/")
        import threading

        Threads.append(MyThread(node))
        Threads[i-2].start()
        # thread = MyThread(node)
        # thread.start()

# to not display stdout in the mininet window
def stopThread(self, line):
    for t in Threads:
        stop_thread(t)

def stop_thread(thread):
    _async_raise(thread.ident, SystemExit)

def _async_raise(tid, exctype):
    """raises the exception, performs cleanup if needed"""
    tid = ctypes.c_long(tid)
    if not inspect.isclass(exctype):
        exctype = type(exctype)
    res = ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, ctypes.py_object(exctype))
    if res == 0:
        raise ValueError("invalid thread id")
    elif res != 1:
        # """if it returns a number greater than one, you're in trouble,
        # and you should call it again with exc=NULL to revert the effect"""
        ctypes.pythonapi.PyThreadState_SetAsyncExc(tid, None)
        raise SystemError("PyThreadState_SetAsyncExc failed")

class MyThread(threading.Thread):
    def __init__(self, node):
        threading.Thread.__init__(self)
        self.node = node
    def run(self):
        self.node.cmd("sh start.sh")

# send ctrl+c to all node's cmd
def stopTest(self, line):
    for node in self.mn.values():
        node.sendInt()

if __name__ == '__main__':
    setLogLevel( 'info' )
    myNetwork()

