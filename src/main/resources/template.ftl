<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style type="text/css">
        ${style}
    </style>
    <title>${title}</title>
    <#function class bool>
        <#if bool>
            <#return "hide" />
        <#else>
            <#return "empty" />
        </#if>
    </#function>
</head>
<body>
<div class="warning">${warning}</div>
<#list services as service>
    <main>
        <header>
            <img src="${service["PHOTO"]}" alt="${service["ACCOUNT"].mail}">
            <div>
                <h1>${service["ACCOUNT"].name}</h1>
                <p>${service["ACCOUNT"].mail}</p>
            </div>
        </header>
        <section>
            <#list settings as setting>
                <#if setting.value>
                    <article class="${class(service[setting.name]?has_content)}">
                        <aside>
                            <img src="${setting.image}" alt="${setting.name}">
                            <a class="hider">${hide}</a>
                        </aside>
                        <div>
                            <h2>${setting.lang}</h2>
                            <a class="hider">${show}</a>
                            <ul>
                                <#list service[setting.name] as entry>
                                    <li>
                                        <#switch setting.name>
                                            <#case "like">
                                            <#case "dislike">
                                                <a href="${"https://www.youtube.com/watch?v="+entry.id}">${entry.name}</a>
                                                <#break>
                                            <#case "subscription">
                                                <a href="${"https://www.youtube.com/channel/"+entry.id}">${entry.name}</a>
                                                <#break>
                                            <#case "playlist">
                                                <a href="${"https://www.youtube.com/playlist?list="+entry.id}">${entry.name}</a>
                                                <#break>
                                            <#case "calendar">
                                                <a href="${"https://calendar.google.com/calendar/embed?src="+entry.id}">${entry.name}</a>
                                                <#break>
                                            <#default>
                                                ${entry.name}
                                        </#switch>
                                        <#if entry.content??>
                                            <ul>
                                                <#list entry.content as entrybis>
                                                    <li>
                                                        <#switch setting.name>
                                                            <#case "playlist">
                                                                <a href="${"https://www.youtube.com/watch?v="+entrybis.id+"&list="+entry.id}">${entrybis.name}</a>
                                                                <#break>
                                                            <#case "calendar">
                                                                <a href="${entrybis.id}">${entrybis.name}</a>
                                                                <#break>
                                                            <#default>
                                                                ${entrybis.name}
                                                        </#switch>
                                                    </li>
                                                <#else>
                                                    <span>${empty}</span>
                                                </#list>
                                            </ul>
                                        </#if>
                                    </li>
                                <#else>
                                    <span>${empty}</span>
                                </#list>
                            </ul>
                        </div>
                    </article>
                </#if>
            </#list>
        </section>
    </main>
</#list>
<script type="text/javascript">
    ${script}
</script>
</body>
</html>
