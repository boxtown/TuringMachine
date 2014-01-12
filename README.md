TuringMachine
============

A basic one tape implementation of a Turing Machine in Java because why not?

Input
=====

Reads from a text file 'TM.txt'
First line must be a list of space delimited numbers identifying accepting states  
following lines are all transitions of the form:  
(CurrentStateNum, CharAtHead) (NextStateNum, CharToWrite, Movement)  

StateNums are obviously numbers. The tape characters must be single characters  
Movement only supports L or R for left or right because we all know that  
a Turing Machine that can also support a stay is no more powerful.
