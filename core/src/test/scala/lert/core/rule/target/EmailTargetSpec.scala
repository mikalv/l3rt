package lert.core.rule.target

import lert.core.BaseSpec
import lert.core.config._
import org.jvnet.mock_javamail.Mailbox

class EmailTargetSpec extends BaseSpec {
  it should "send an email" in {
    Mailbox.clearAll()
    implicit val config = SimpleConfigProvider(
      Config().copy(targetSettings = TargetSettings().copy(mailServer = MailServerSettings("localhost", "123", true, "test@test.com", "123")))
    ).conf

    val emailTarget = new EmailTarget()

    emailTarget.send("test@gmail.com", "subj", "body")

    assert(Mailbox.get("test@gmail.com").getNewMessageCount == 1)
    assert(Mailbox.get("test@gmail.com").get(0).getSubject == "subj")
    assert(Mailbox.get("test@gmail.com").get(0).getContent == "body")
  }
}
