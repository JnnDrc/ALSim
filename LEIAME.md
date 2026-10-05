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

### CAK

### GUI