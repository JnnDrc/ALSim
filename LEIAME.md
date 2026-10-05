# AL.Sim

Simulador de circuitos simples como trabalho de Algebra Linear

## Tutorial

Para desenhar os circuitos, selecione o componente que quer colocar usando
o menu superior ou o menu do clique direito, ou opcinalmente as teclas de atalho

Para simular, utilize a tecla F1 ou va em simulation -> simulate, também pode
ativar a simulação automática em simulation -> live refresh (possiveis bugs)

## Detalhes técnicos

Esta seção explica o funcionamento dessa grande gambiarra com um pouco
de código no meio

O projeto é dividido em três modulos, Circuit Analysis Kernel (CAK) é o nucleo de
simulção e é responsavel por realizar a Analise Nodal Modificada, GUI é a parte
gráfica da aplicação onde se encontra sua maior parte e as maiores disgraças que
ja escrevi em minha vida, e por fim UTIL é onda esta o resto.

### Main

Esta aplicação é feita usando JavaFX, uma framework para criar aplicações graficas
em Java, por isso, o entrypoint é um pouco diferente do habitual.

A classe main é responsavel por chamar a inicialização do JavaFX com o método
Application.launch, usando a classe ALSimApp.

A classe ALSimApp extende a classe Application do JavaFX, ela é responsavel por
inicializar o Stage, usando a Scene criada pelo MainWindow, caso não saiba o que
essas palavras significam, veja [a documentação de JavaFX](https://fxdocs.github.io/docs/html5/#_introduction)

Por fim, a classe MainWindow é parte do modulo gui, é responsavel por inicializar
toda a parte gráfica da aplicação, o canvas e o editor, e é onde reside a casa de
todas as desgraças deste programa!

### UTIL

O modulo util possui algumas definições de classes que não cabem nas outras, mas
que são uteis no projeto, o seu principal uso é para a realização da matematica
com as classes Vector e Matrix, junto com a interface LinearSolver.

Vector e Matrix apenas implementam um Vetor e uma Matriz respectivamente, junto
com algumas operações para facilitar o uso em algoritmos de eliminação.

A interface LinearSolver define um unico método solve, que tem como parametros
a matriz A e o vetor b, seu papel é resolver sistemas lineares. A principal 
implementação utilizada é a classe GaussJordanSolver, que resolve o sistema
com eliminação de Gauss-Jordan, também está implementado uma resolução por Gauss-Seidel
na classe GaussSeidelSolver, que não é utilizada em nenhum lugar pois não serve
para o tipo de sistemas gerado pela Análise Nodal Modificada. Por mais que a
classe exista, LUSolver não esta implementada e possui apenas um to-do que nunca
foi did.  
ATUALIZAÇÃO: Agora é usada apenas uma simples Eliminação de Gauss com
retrosubstituição, implementada na classe GaussianSolver.

Engineering é a classe que cuida da leitura e formatação de valores na
notação de "engenharia", também conhecido como os prefixos do SI.


### CAK

Circuit Analysis Kernel é a parte que realiza a análise de circuitos, coisa
que soa extremamente complicada mas é de longe a coisa mais simples desse
projeto.

Circuit é basicamente uma coleção de nós e componentes.

Component é a classe abstrata base para todas as classes de componentes, seu
método mais importante é o stamp(), que define como o componente contribui
para o sistema do circuito

Node também é uma classe fundamental visto que estamos fazendo análise 
NODAL aqui, ele serve de âncora da análise.

Resistor e VSource são as classes para, bom, resistor e fonte de tensão.

NetlistParser é a classe que é responsavel por ler as netlists (.ckt) usadas
para descrever o circuito elétrico, baseado nas netlists do [ngspice](https://ngspice.sourceforge.io/).

Modified Nodal Analysis ou MNA é a classe responsavel por fazer a análise nodal,
ela basicamente faz o stamp dos componentes do circuito, e então resolve o circuito

### GUI

Esta é a parte mais complicada deste código.
Em resumo:
- elements :: Define os elementos graficos do simulador (componentes, fios, elementos especiais, ...)
- simulate :: É a parte que faz a integração entre o ambiente gráfico e o CAK
- tools :: Ferramentas de desenho de componentes
- ui :: Elementos de UI especias (painéis e dialogs)
- CircuitCanvas :: Gerencia o ambiente gráfico
- CircuitContext :: Une Editor e Viewport em uma única classe para ser usado pelo Canvas
- CircuitEditor :: Gerencia a representação interna dos circuitos elétricos desenhados
- Viewport :: Controle do campo de visão (camera) do ambiente gráfico
- CircuitLoader :: Cuida de salvar e restaurar circuitos em arquivos no disco
- NetlistGenerator :: Gera a netlist para ser usada pelo CAK
- NetResolver :: Resolve minha má arquitetura mapeando nós do CircuitEditor para nós do CAK (sim, eles não são iguais)
fazendo union-find nos nós e criando os mapas
- MainWindow :: Janela gráfica do programa