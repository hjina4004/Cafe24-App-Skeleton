helperCommon = (() => {
    const __version = "2023092500";

    const __debug = localStorage.getItem("lmfds_debug") === "1";
    const logger = (...args) => (__debug ? console.log(...args) : false);

    const init = () => {
        logger("helperCommon", __version);
    };

    init();
    return {
        logger: logger,
    };
})();
