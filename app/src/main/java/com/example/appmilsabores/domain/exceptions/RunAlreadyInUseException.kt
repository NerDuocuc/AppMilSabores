package com.example.appmilsabores.domain.exceptions

class RunAlreadyInUseException(run: String) : IllegalStateException("El RUN $run ya está registrado")
