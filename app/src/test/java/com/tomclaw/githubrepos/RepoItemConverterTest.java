package com.tomclaw.githubrepos;

import com.tomclaw.githubrepos.dto.Repo;
import com.tomclaw.githubrepos.main.RepoItemConverter;
import com.tomclaw.githubrepos.main.list.RepoItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class RepoItemConverterTest {

    private PodamFactory factory;

    private RepoItemConverter converter;

    @Parameterized.Parameter
    public boolean isLoading;

    @Parameterized.Parameter(1)
    public boolean isError;

    @Parameterized.Parameter(2)
    public boolean isProgressExpected;

    @Parameterized.Parameter(3)
    public boolean isErrorExpected;

    @Before
    public void init() {
        factory = new PodamFactoryImpl();
        converter = new RepoItemConverter.RepoItemConverterImpl();
    }

    @Test
    public void convert_correctItemConvert() throws Exception {
        Repo firstRepo = randomRepo();
        Repo lastRepo = randomRepo();
        List<Repo> repoList = asList(firstRepo, lastRepo);

        List<RepoItem> repoItemList = converter.convert(repoList, isLoading, isError);

        assertEquals(2, repoItemList.size());
        RepoItem firstRepoItem = repoItemList.get(0);
        assertEquals(firstRepo.getName(), firstRepoItem.getName());
        assertEquals(firstRepo.getDescription(), firstRepoItem.getDescription());
        assertEquals(firstRepo.getUrl(), firstRepoItem.getRepoUrl());
        assertEquals(firstRepo.getOwner().getLogin(), firstRepoItem.getOwnerLogin());
        assertEquals(firstRepo.getOwner().getUrl(), firstRepoItem.getProfileUrl());
        RepoItem lastRepoItem = repoItemList.get(1);
        assertEquals(isProgressExpected, lastRepoItem.isShowProgress());
        assertEquals(isErrorExpected, lastRepoItem.isShowError());
    }

    private Repo randomRepo() {
        return factory.manufacturePojo(Repo.class);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {false, false, false, false},
                {false, true, false, true},
                {true, false, true, false},
                {true, true, false, true}
        });
    }

}