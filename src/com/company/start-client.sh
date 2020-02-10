#java -cp /Users/des/rmidemo/compute.jar:. -Djava.security.policy=client.policy client.ComputePi 20345 45
java -cp /home/pi/Documents/distributed_systems/sample_rmi/ -Djava.rmi.server.codebase=file:/home/pi/Documents/distributed_systems/sample_rmi/ -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy client.ComputePi 20345 45
