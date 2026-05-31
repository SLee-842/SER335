#!/usr/bin/python3

import os
import time

for i in range(5):
	os.system("docker exec -it seed-attacker1 python3 /volumes/Attacker1_ICMPPacket_Spoofer.py")
	print("1 packet sent from attacker 1 !!")
	time.sleep(1)
	os.system("docker exec -it seed-attacker2 python3 /volumes/Attacker2_ICMPPacket_Spoofer.py")
	print("1 packet sent from attacker 2 !!")
	time.sleep(1)

	
