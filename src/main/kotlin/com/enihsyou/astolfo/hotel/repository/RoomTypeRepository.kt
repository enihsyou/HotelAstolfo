package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.RoomType
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository("房间类型仓库")
@RepositoryRestResource(path = "roomTypes")
interface RoomTypeRepository : PagingAndSortingRepository<RoomType, Int> {

    fun findByType(type: String): RoomType?
}
