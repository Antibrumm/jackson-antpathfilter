package ch.mfrey.jackson.antpathfilter.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.mfrey.jackson.antpathfilter.test.Judgement.Courthouse;

public class AntPathConvertTest {

    @Test
    public void testConvert() throws JsonProcessingException {
        List<Judgement> judgements = new ArrayList<Judgement>();
        Judgement judgement = new Judgement();
        judgement.setId(1);
        judgement.setJudgementDate(new Date());
        judgement.setJudgementNo("1");
        Courthouse courthouse = new Courthouse();
        courthouse.setId(1);
        courthouse.setName("Courthouse 1");
        judgement.setCourthouse(courthouse);
        judgements.add(judgement);

        judgement = new Judgement();
        judgement.setId(2);
        judgement.setJudgementDate(new Date());
        judgement.setJudgementNo("2");
        courthouse = new Courthouse();
        courthouse.setId(2);
        courthouse.setName("Courthouse 2");
        judgement.setCourthouse(courthouse);
        judgements.add(judgement);

        String[] includedFieldNames = { "id", "judgementNo", "judgementDate", "courthouse", "courthouse.name",
                "@loaded" };
        ObjectMapper mapper = ObjectMapperBuilder.buildObjectMapper(includedFieldNames);

        String result = mapper.writeValueAsString(judgements);
        System.out.println(result);

        com.fasterxml.jackson.databind.type.CollectionType collectionType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Object.class);

        List<Object> map = mapper.convertValue(judgements, collectionType);
        System.out.println(map);
    }
}
