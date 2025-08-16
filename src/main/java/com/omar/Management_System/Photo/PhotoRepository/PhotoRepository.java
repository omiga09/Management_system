package com.omar.Management_System.Photo.PhotoRepository;

import com.omar.Management_System.Photo.Photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}