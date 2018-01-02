package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Comment
import org.springframework.data.repository.PagingAndSortingRepository

interface CommentRepository:PagingAndSortingRepository<Comment, Int>
