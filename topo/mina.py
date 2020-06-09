#!/usr/bin/python

from mininet.node import Controller, OVSKernelSwitch
from mininet.log import setLogLevel, info
from mn_wifi.net import Mininet_wifi
from mn_wifi.node import Station, OVSKernelAP
from mn_wifi.cli import CLI
from mn_wifi.link import wmediumd, adhoc
from mn_wifi.wmediumdConnector import interference
from subprocess import call


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
    sta6 = net.addStation('sta6', ip='10.0.0.6',
                           position='448.0,169.0,0')
    sta11 = net.addStation('sta11', ip='10.0.0.11',
                           position='993.0,258.0,0')
    sta4 = net.addStation('sta4', ip='10.0.0.4',
                           position='819.0,196.0,0')
    sta27 = net.addStation('sta27', ip='10.0.0.27',
                           position='149.0,572.0,0')
    sta5 = net.addStation('sta5', ip='10.0.0.5',
                           position='488.0,547.0,0')
    sta19 = net.addStation('sta19', ip='10.0.0.19',
                           position='722.0,752.0,0')
    sta12 = net.addStation('sta12', ip='10.0.0.12',
                           position='987.0,539.0,0')
    sta25 = net.addStation('sta25', ip='10.0.0.25',
                           position='100.0,386.0,0')
    sta26 = net.addStation('sta26', ip='10.0.0.26',
                           position='365.0,724.0,0')
    sta20 = net.addStation('sta20', ip='10.0.0.20',
                           position='1039.0,40.0,0')
    sta18 = net.addStation('sta18', ip='10.0.0.18',
                           position='551.0,750.0,0')
    sta13 = net.addStation('sta13', ip='10.0.0.13',
                           position='790.0,50.0,0')
    sta24 = net.addStation('sta24', ip='10.0.0.24',
                           position='1251.0,542.0,0')
    sta1 = net.addStation('sta1', ip='10.0.0.1',
                           position='617.0,382.0,0')
    sta21 = net.addStation('sta21', ip='10.0.0.21',
                           position='1066.0,387.0,0')
    sta17 = net.addStation('sta17', ip='10.0.0.17',
                           position='1168.0,218.0,0')
    sta23 = net.addStation('sta23', ip='10.0.0.23',
                           position='133.0,753.0,0')
    sta22 = net.addStation('sta22', ip='10.0.0.22',
                           position='895.0,736.0,0')
    sta15 = net.addStation('sta15', ip='10.0.0.15',
                           position='942.0,122.0,0')
    sta16 = net.addStation('sta16', ip='10.0.0.16',
                           position='1140.0,675.0,0')
    sta8 = net.addStation('sta8', ip='10.0.0.8',
                           position='140.0,106.0,0')
    sta9 = net.addStation('sta9', ip='10.0.0.9',
                           position='574.0,47.0,0')
    sta2 = net.addStation('sta2', ip='10.0.0.2',
                           position='378.0,375.0,0')
    sta29 = net.addStation('sta29', ip='10.0.0.29',
                           position='117.0,226.0,0')
    sta7 = net.addStation('sta7', ip='10.0.0.7',
                           position='781.0,564.0,0')
    sta10 = net.addStation('sta10', ip='10.0.0.10',
                           position='387.0,36.0,0')
    sta3 = net.addStation('sta3', ip='10.0.0.3',
                           position='848.0,373.0,0')
    sta28 = net.addStation('sta28', ip='10.0.0.28',
                           position='328.0,499.0,0')
    sta14 = net.addStation('sta14', ip='10.0.0.14',
                           position='1196.0,38.0,0')

    net.plotGraph(max_x=2000, max_y=2000)

    info("*** Configuring Propagation Model\n")
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
    net.addLink(sta1, cls=adhoc, intf='sta1-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta2, cls=adhoc, intf='sta2-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta3, cls=adhoc, intf='sta3-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta4, cls=adhoc, intf='sta4-wlan0',
                ssid='adhocNet', mode='g', channel=5)
    net.addLink(sta5, cls=adhoc, intf='sta5-wlan0',
                ssid='adhocNet', mode='g', channel=5)

    info( '*** Starting network\n')
    net.build()
    info( '*** Starting controllers\n')
    for controller in net.controllers:
        controller.start()

    info( '*** Starting switches/APs\n')
    net.get('s10').start([])
    net.get('s3').start([])
    net.get('s11').start([])
    net.get('s4').start([])
    net.get('s7').start([])
    net.get('s12').start([])
    net.get('s8').start([])
    net.get('s5').start([])
    net.get('s6').start([])
    net.get('s1').start([])
    net.get('s9').start([])
    net.get('s2').start([])

    info( '*** Post configure nodes\n')

    CLI(net)
    net.stop()


if __name__ == '__main__':
    setLogLevel( 'info' )
    myNetwork()

