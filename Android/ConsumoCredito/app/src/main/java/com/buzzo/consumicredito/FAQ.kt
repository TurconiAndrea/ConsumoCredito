package com.buzzo.consumicredito

class FAQ (
    val question: String,
    val answer: String,
    val topic: String ) {

    companion object {

        fun getFirstTwoFAQ(): List<FAQ> {
            return listOf (
                FAQ ("Come utilizzare l'app?",
                    "Per utilizzare l'app è necessario inserire le credenziali che sono state fornite al momento della creazione e dell'attivazione della SIM di Iliad.",
                    "Tutorial"),
                FAQ ("È l'app ufficiale di Iliad?",
                    "No, non è assolutamente l'app di Iliad ufficiale. Questa app è stata sviluppata per motivi di studio e senza ricavarne alcun profitto.",
                    "Domande Frequenti")
            )
        }
    }

}
