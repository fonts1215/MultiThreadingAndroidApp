# HW2
## Esercizio in ambiente Android sul multitasking

Per la soluzione di questo esercizio e' stato proposto una applicazione android che permette di effettuare delle chiamate HTTP dove e' possibile configurare la url da richiamare, il numero di thread che si dovranno occupare delle chiamate ed il numero di richieste che si vogliono fare.

![application screen](https://github.com/fonts1215/MultiThreadingAndroidApp/blob/main/images/screen1.png)

All'interno della prima "TextField" e' possibile selezionare l'url che si vuole sottoporre alla get.
Tramite i bottoni di threadcount e' possibile aumentare e diminuire il numero di thread che devono elaborare le richieste. Anch'esse possono essere determinate tramite i pulsanti piu' o meno. 
Nella sezione inferiore dello schermo e' presente una textArea che contiene i log delle vare chiamate che vengono effettuate.
Al termine dell'esecuzione delle varie richieste l'applicazione rispondera' con una notifica toast la quale conterra' il tempo di esecuzione dell'operazione. Considerando che le varie richieste vengono suddivise nei vari thread, a parita' di numero di richieste e aumentando il numero di thread, l'operazione nel totale impieghera' minor tempo. 
