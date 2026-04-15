package csu.edu.fbszzzz.service;

import csu.edu.fbszzzz.entity.Member;
import csu.edu.fbszzzz.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 成员服务测试类
 */
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        // 准备测试数据
        Member member = new Member();
        member.setUsername("testuser");
        member.setPassword("password123");
        member.setRealName("测试用户");
        member.setEmail("test@example.com");
        member.setPhone("13800138000");

        // 模拟用户名不存在
        when(memberMapper.findByUsername("testuser")).thenReturn(null);
        // 模拟插入成功，返回ID为1
        when(memberMapper.insert(any(Member.class))).thenAnswer(invocation -> {
            Member m = invocation.getArgument(0);
            m.setId(1L);
            return 1;
        });

        // 执行注册
        Member result = memberService.register(member);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertNotNull(result.getRegisterTime());
        assertEquals(1, result.getStatus());

        // 验证方法调用
        verify(memberMapper).findByUsername("testuser");
        verify(memberMapper).insert(any(Member.class));
    }

    @Test
    void testRegisterDuplicateUsername() {
        // 准备测试数据
        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setUsername("existinguser");

        Member newMember = new Member();
        newMember.setUsername("existinguser");
        newMember.setPassword("password123");

        // 模拟用户名已存在
        when(memberMapper.findByUsername("existinguser")).thenReturn(existingMember);

        // 执行注册，应该抛出异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            memberService.register(newMember);
        });

        assertEquals("用户名已存在: existinguser", exception.getMessage());

        // 验证insert方法没有被调用
        verify(memberMapper).findByUsername("existinguser");
        verify(memberMapper, never()).insert(any(Member.class));
    }

    @Test
    void testFindById() {
        // 准备测试数据
        Member member = new Member();
        member.setId(1L);
        member.setUsername("testuser");

        // 模拟查询结果
        when(memberMapper.findById(1L)).thenReturn(member);

        // 执行查询
        Member result = memberService.findById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());

        // 验证方法调用
        verify(memberMapper).findById(1L);
    }

    @Test
    void testFindAll() {
        // 准备测试数据
        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("user1");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("user2");

        // 模拟查询结果
        when(memberMapper.findAll()).thenReturn(Arrays.asList(member1, member2));

        // 执行查询
        List<Member> result = memberService.findAll();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());

        // 验证方法调用
        verify(memberMapper).findAll();
    }
}