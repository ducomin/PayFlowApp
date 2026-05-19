package br.com.payflowapplication.data.db

import androidx.room.TypeConverter
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade

class Converters {

    @TypeConverter
    fun fromModalidade(value: Modalidade): String = value.name

    @TypeConverter
    fun toModalidade(value: String): Modalidade = Modalidade.valueOf(value)

    @TypeConverter
    fun fromCategoria(value: CategoriaAssinatura): String = value.name

    @TypeConverter
    fun toCategoria(value: String): CategoriaAssinatura = CategoriaAssinatura.valueOf(value)
}

