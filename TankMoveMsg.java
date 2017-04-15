package com.Tanknet.Gan;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg{
	
	int msgType = Msg.TANK_MOVE_MSG;
	int id;
	Dir dir;
	private TankClient tc;
	
	public TankMoveMsg(int id, Dir dir) {
		this.id = id;
		this.dir = dir;
	}


	public TankMoveMsg(TankClient tc) {
		// TODO 自动生成的构造函数存根
		this.tc = tc;
	}


	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buff = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buff,buff.length,new InetSocketAddress(IP,udpPort));
		try {
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void parse(DataInputStream dis) {
		try{
			int id = dis.readInt();
			if(tc.myTank.id == id)
				return;
			Dir dir = Dir.values()[dis.readInt()];
			boolean exist = false;
			for (int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					t.dir = dir;
					exist = true;
					break;
				}
				//其他处理
			}
		} catch (IOException e){
			System.out.println("TankMoveMsg error");
		}
	}
	
	
	
}
