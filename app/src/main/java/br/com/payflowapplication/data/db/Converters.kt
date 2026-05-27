package br.com.payflowapplication.data.db

import androidx.room.TypeConverter
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): Long? = value?.toEpochDay()

    @TypeConverter
    fun toLocalDate(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun fromModalidade(value: Modalidade): String = value.name

    @TypeConverter
    fun toModalidade(value: String): Modalidade = Modalidade.valueOf(value)

    @TypeConverter
    fun fromCategoria(value: CategoriaAssinatura): String = value.name

    @TypeConverter
    fun toCategoria(value: String): CategoriaAssinatura = CategoriaAssinatura.valueOf(value)
}
