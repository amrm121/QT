# siscomRfidQTsimulator
um simulador em JAVA de QueryTree, QueryTree shortcutting, QueryTree Quaternario, QueryTree shortcutting Quaternario
no qual comparamos suas perfomances ao identificar Tags unicas.

Segue abaixo as exigências do projeto: 
http://www.cin.ufpe.br/~pasg/if740/projeto-2019-1.pdf

Algoritmos utilizados do artigo abaixo:
http://cocoa.ethz.ch/downloads/2014/06/None_MIT-AUTOID-TR-003.pdf

Para utilizar o projeto, basta clonar o repositório e instalar no máquina o GNUPLOT

link para windows: https://sourceforge.net/projects/gnuplot/

linha de comando para linux: ```sudo apt-get install gnuplot```

Após instalação do GNUPLOT, basta rodar o programa em algum compilador de java, ou pelo executar.jar, ou adicionar a alguma IDE java e rodar o projeto. 

O programa gera os arquivos de textos dos dados gerados
E gera as imagens dos gráficos na pasta do projeto.
Se executar pelo .jar, aguarde um tempo curto que os arquivos serão gerados

Para adicionar um outro ALGORITMO e comparar basta adicionar ao array ```Resultados``` os resultados do algoritmo novo
e aumentar o size do array. Com isso ele já automaticamente plota o novo algoritmo comparando com os já existentes.
