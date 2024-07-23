***** Instruções para a compilação, execução e testagem ***** 

  

Este projeto contém as duas classes que foram pedidas no enunciado. A classe MyHttpServer contém duas "inner classes" que funcionam como auxiliares, MyHttpServerThread e HttpRequest. Para testar e avaliar este projeto é suficiente seguir as instruções presentes na secção "Como Testar o Próprio Código" do enunciado, que também são reportadas aqui em baixo. 

  

Passo1: Compilar as duas classes, “$ javac MyHttpClient.java MyHttpServer.java”  

  

Passo2: Se a compilação correr bem, colocar no mesmo diretório e compilar a classe TestMp1 “$ javac TestMp1.java”  

  

Passo3: Abrir um terminal e lançar a classe servidor “$ java MyHttpServer 5555” (A classe MyHttpServer pede número de porto TCP, no qual vai ficar a aceitar conexões, como parâmetro. Neste exemplo é usado o número de porto 5555).  

  

Passo4: Abrir um outro terminal e lançar a classe de teste “$ java TestMp1 localhost 5555”  

  

Passo5: Usar o menu interativo da classe de teste para gerar os diferentes pedidos do cliente e as respostas do servidor, e depois de cada troca pedido/resposta analisar os respetivos logs na consola (stdout) para verificar que os comportamentos sejam conformes aos esperados.   

  

Passo6: Para testar a funcionalidade 1.f do servidor, podem ser lançadas diferentes instâncias da classe TestMp1 até atingir o limite especificado no enunciado e tentar lançar e usar uma ulterior instância da classe para interagir com o servidor. O servidor deverá reagir com um erro aos pedidos da última instância lancada enquanto todas as outras estejam ativas. Se uma delas se desconectar, os pedidos desta deverão passar a ser processados normalmente.  

  

***** Funcionalidades ***** 

  

O projeto implementa corretamente todas as funcionalidades pedidas no enunciado.
Quando o cliente envia um pedido GET bem formatado que pede o URL /index.html, ao receber a resposta do servidor, o cliente cria um ficheiro HTML com o corpo da resposta, e abre este no browser.
