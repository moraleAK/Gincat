package net.gincat.demo.jpx.repository;

import net.gincat.demo.jpx.entity.Student;
import net.gincat.jpax.JpaRepositoryExtend;

/**
 * @author Gin
 * @since 2020/11/30 10:40
 */
public interface StudentRepository extends JpaRepositoryExtend<Student, Long> {
}
