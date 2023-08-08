package loudvoice.timeLessons;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ILessonService {

    List<timeLessons> getAllTime();

    Optional<timeLessons> findById(Long id);

    @Transactional
    List<timeLessons> getRecordedTime();
    @Transactional
    List<timeLessons> getLessTime();
    @Transactional
    List<timeLessons> getAllMyTime();

    @Transactional
    void addDateId(String Uid, Long id);
    @Transactional
    void DeleteDateId(String Uid, Long id);
    @Transactional
    void deleteLessonsTime(Long id);
    @Transactional
    void deleteTime(Long id);
}
