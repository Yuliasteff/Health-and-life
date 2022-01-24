package by.iapsit.healthandlife.domain.util

interface GenericMapper<Entity, Dto> {

    fun fromEntity(entity: Entity): Dto
    fun toEntity(dto: Dto): Entity

}