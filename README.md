# chat-multicast-java-ibellacn

## Multicast Chat
Aplicação de Chat em Java utilizando o protocolo Multicast

## Autora
Isabella Carine Cruz Nicácio

## Professor
Hugo Bastos de Paula

## Requisitos básicos da aplicação
- O servidor deve gerenciar múltiplas salas de bate papo;
- O cliente deve ser capaz de solicitar a lista de salas;
- O cliente deve ser capaz de solicitar acesso à uma das salas de bate papo;
- O servidor deve manter uma lista dos membros da sala;
- O cliente deve ser capaz de enviar mensagens para a sala;
- O cliente deve ser capaz de sair da sala de bate papo.

## Instruções de utilização

1. Compilar e executar a classe Server (Server.java).
2. Compilar a classe Client (Client.java) e ChatRoom (ChatRoom.java).
3. Executar a classe Client.

Código para compilar
```
javac -cp ../ src/lab_mob_dist/Server.java
javac -cp ../ src/lab_mob_dist/Client.java
javac -cp ../ src/lab_mob_dist/ChatRoom.java
```

Código para executar
```
java -cp ../ src/lab_mob_dist/Server.java
java -cp ../ src/lab_mob_dist/Client.java
```

*OBS: Necessário executar a classe Server antes de executar o Client.*

Siga as instruções exibidas no console para as operações de criar e entrar na sala, listar salas e membros, enviar mensagens e sair do chat.

chat-multicast-java-ibellacn created by GitHub Classroom
