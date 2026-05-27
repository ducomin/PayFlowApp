package br.com.payflowapplication.data.db

import androidx.room.TypeConverter
import br.com.payflowapplication.model.CategoriaAssinatura
import br.com.payflowapplication.model.Modalidade
import br.com.payflowapplication.model.TipoNotificacao
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun fromTipoNotificacao(value: TipoNotificacao): String = value.name

    @TypeConverter
    fun toTipoNotificacao(value: String): TipoNotificacao = TipoNotificacao.valueOf(value)

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
